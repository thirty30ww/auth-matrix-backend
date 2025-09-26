package com.thirty.user.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua_parser.Client;
import ua_parser.Parser;

/**
 * 用户代理工具类
 * 用于解析用户代理字符串，获取设备型号、操作系统和浏览器信息
 */
@Component
@Slf4j
public class UserAgentUtil {
    /**
     * ua-parser解析器实例
     * 使用单例模式，避免重复创建解析器
     */
    private final Parser parser;

    public UserAgentUtil() {
        this.parser = new Parser();
    }

    /**
     * User-Agent解析结果封装类
     * 包含设备型号、操作系统、浏览器三个主要信息
     */
    @Data
    @AllArgsConstructor
    public static class UserAgentInfo {
        private String deviceModel;
        private String operatingSystem;
        private String browser;
    }

    /**
     * 解析用户代理字符串，获取设备型号、操作系统和浏览器信息
     *
     * @param userAgent 用户代理字符串
     * @return UserAgentInfo 包含设备型号、操作系统、浏览器的封装对象
     */
    public UserAgentInfo parseUserAgent(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return new UserAgentInfo("UNKNOWN", "UNKNOWN", "UNKNOWN");
        }

        try {
            // 使用ua-parser解析User-Agent字符串
            Client client = parser.parse(userAgent);

            // 构建设备型号信息
            String deviceModel = buildDeviceModel(client);

            // 构建操作系统信息
            String operatingSystem = buildOperatingSystem(client);

            // 构建浏览器信息
            String browser = buildBrowser(client);

            return new UserAgentInfo(deviceModel, operatingSystem, browser);
        } catch (Exception e) {
            // 解析失败时记录警告日志，并返回默认值
            log.warn("解析User-Agent失败，User-Agent: {}, 错误信息: {}", userAgent, e.getMessage());
            return new UserAgentInfo("UNKNOWN", "UNKNOWN", "UNKNOWN");
        }
    }

    /**
     * 构建设备型号信息
     * 格式：操作系统名称 + 主版本号 + 次版本号 + 补丁版本号
     * 例如：Windows 10, macOS 12.6, Android 11
     *
     * @param client ua-parser解析后的Client对象
     * @return 设备型号字符串，若未解析到则返回"UNKNOWN"
     */
    private String buildDeviceModel(Client client) {
        StringBuilder os = new StringBuilder();

        log.debug("构建设备型号: {}", client);
        // 检查操作系统family是否有效
        if (client.os.family != null && !"Other".equals(client.os.family)) {
            // 使用映射方法获取友好的操作系统名称
            String osName = mapOsName(client.os.family);
            os.append(osName);

            // 添加版本号
            if (client.os.major != null) {
                // Windows版本特殊处理
                if ("Windows".equals(client.os.family)) {
                    String version = mapWindowsVersion(client.os.major, client.os.minor, client.os.patch);
                    os.append(version);
                } else {
                    // 其他系统正常显示版本号
                    os.append(" ").append(client.os.major);
                    if (client.os.minor != null) {
                        os.append(".").append(client.os.minor);
                        if (client.os.patch != null) {
                            os.append(".").append(client.os.patch);
                        }
                    }
                }
            }
        } else {
            os.append("UNKNOWN");
        }
        return os.toString();
    }

    /**
     * 构建操作系统信息
     * @param client ua-parser解析后的Client对象
     * @return 操作系统字符串，若未解析到则返回"UNKNOWN"
     */
    private String buildOperatingSystem(Client client) {
        if (client.os.family != null && !"Other".equals(client.os.family)) {
            // 使用映射方法获取友好的操作系统名称
            return mapOsName(client.os.family);
        } else {
            return "UNKNOWN";
        }
    }

    /**
     * 构建浏览器信息
     *
     * @param client ua-parser解析后的Client对象
     * @return 浏览器字符串，若未解析到则返回"UNKNOWN"
     */
    private String buildBrowser(Client client) {
        // 检查浏览器family是否有效
        if (client.userAgent.family != null && !"Other".equals(client.userAgent.family)) {
            return client.userAgent.family;
        } else {
            return "UNKNOWN";
        }
    }

    /**
     * 操作系统名称映射
     * @param osFamily 原始操作系统名称
     * @return 映射后的操作系统名称
     */
    private String mapOsName(String osFamily) {
        if (osFamily == null) return "UNKNOWN";
        return "Mac OS X".equals(osFamily) ? "macOS" : osFamily;
    }

    /**
     * Windows版本号映射
     * @param major 主版本号
     * @param minor 次版本号
     * @param patch 补丁版本号
     * @return Windows版本名称
     */
    private String mapWindowsVersion(String major, String minor, String patch) {
        if ("10".equals(major)) {
            // Windows 10和11都是10.0，通过build number区分
            // Windows 11的build number从22000开始
            if (StringUtils.isNotBlank(patch)) {
                int buildNumber = Integer.parseInt(patch);
                if (buildNumber >= 22000) {
                    return " 11";
                } else {
                    return " 10";
                }
            }
            // 如果没有patch信息，默认为Windows 10
            return " 10/11";
        } else if ("6".equals(major)) {
            return switch (minor) {
                case "3" -> " 8.1";
                case "2" -> " 8";
                case "1" -> " 7";
                case "0" -> " Vista";
                default -> " " + major + "." + minor;
            };
        } else if ("5".equals(major) && ("1".equals(minor) || "2".equals(minor))) {
            return " XP";
        }
        return " " + major + (minor != null ? "." + minor : "");
    }
}
