package com.texasgamer.zephyr;

import com.texasgamer.zephyr.util.NetworkUtils;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class JoinCodeTests {

    @Test
    public void networkUtils_JoinCodeToIp_ReturnsCorrectIp() {
        // Short
        assertThat(NetworkUtils.joinCodeToIp("1")).isEqualTo("192.168.0.1");
        assertThat(NetworkUtils.joinCodeToIp("12")).isEqualTo("192.168.0.12");
        assertThat(NetworkUtils.joinCodeToIp("123")).isEqualTo("192.168.0.123");
        assertThat(NetworkUtils.joinCodeToIp("001")).isEqualTo("192.168.0.001");

        // Medium
        assertThat(NetworkUtils.joinCodeToIp("1.0")).isEqualTo("192.168.1.0");
        assertThat(NetworkUtils.joinCodeToIp("1.12")).isEqualTo("192.168.1.12");
        assertThat(NetworkUtils.joinCodeToIp("1.123")).isEqualTo("192.168.1.123");
        assertThat(NetworkUtils.joinCodeToIp("12.0")).isEqualTo("192.168.12.0");
        assertThat(NetworkUtils.joinCodeToIp("12.12")).isEqualTo("192.168.12.12");
        assertThat(NetworkUtils.joinCodeToIp("12.123")).isEqualTo("192.168.12.123");
        assertThat(NetworkUtils.joinCodeToIp("123.0")).isEqualTo("192.168.123.0");
        assertThat(NetworkUtils.joinCodeToIp("123.12")).isEqualTo("192.168.123.12");
        assertThat(NetworkUtils.joinCodeToIp("123.123")).isEqualTo("192.168.123.123");

        // IP
        assertThat(NetworkUtils.joinCodeToIp("192.168.0.1")).isEqualTo("192.168.0.1");
        assertThat(NetworkUtils.joinCodeToIp("192.200.23.4")).isEqualTo("192.200.23.4");
        assertThat(NetworkUtils.joinCodeToIp("255.255.255.255")).isEqualTo("255.255.255.255");
    }

    @Test
    public void networkUtils_IpToJoinCode_ReturnsCorrectJoinCode() {
        // Short
        assertThat(NetworkUtils.ipToJoinCode("192.168.0.1")).isEqualTo("1");
        assertThat(NetworkUtils.ipToJoinCode("192.168.0.12")).isEqualTo("12");
        assertThat(NetworkUtils.ipToJoinCode("192.168.0.123")).isEqualTo("123");
        assertThat(NetworkUtils.ipToJoinCode("192.168.0.001")).isEqualTo("001");

        // Medium
        assertThat(NetworkUtils.ipToJoinCode("192.168.1.0")).isEqualTo("1.0");
        assertThat(NetworkUtils.ipToJoinCode("192.168.1.12")).isEqualTo("1.12");
        assertThat(NetworkUtils.ipToJoinCode("192.168.1.123")).isEqualTo("1.123");
        assertThat(NetworkUtils.ipToJoinCode("192.168.12.0")).isEqualTo("12.0");
        assertThat(NetworkUtils.ipToJoinCode("192.168.12.12")).isEqualTo("12.12");
        assertThat(NetworkUtils.ipToJoinCode("192.168.12.123")).isEqualTo("12.123");
        assertThat(NetworkUtils.ipToJoinCode("192.168.123.0")).isEqualTo("123.0");
        assertThat(NetworkUtils.ipToJoinCode("192.168.123.12")).isEqualTo("123.12");
        assertThat(NetworkUtils.ipToJoinCode("192.168.123.123")).isEqualTo("123.123");

        // IP
        assertThat(NetworkUtils.ipToJoinCode("192.200.23.4")).isEqualTo("192.200.23.4");
        assertThat(NetworkUtils.ipToJoinCode("255.255.255.255")).isEqualTo("255.255.255.255");
    }

    @Test
    public void networkUtils_ValidateValidJoinCode_ReturnsTrue() {
        // Short
        assertThat(NetworkUtils.isValidJoinCode("1")).isTrue();
        assertThat(NetworkUtils.isValidJoinCode("01")).isTrue();
        assertThat(NetworkUtils.isValidJoinCode("123")).isTrue();
        assertThat(NetworkUtils.isValidJoinCode("000")).isTrue();

        // Medium
        assertThat(NetworkUtils.isValidJoinCode("1.1")).isTrue();
        assertThat(NetworkUtils.isValidJoinCode("30.1")).isTrue();
        assertThat(NetworkUtils.isValidJoinCode("1.23")).isTrue();
        assertThat(NetworkUtils.isValidJoinCode("123.205")).isTrue();
    }

    @Test
    public void networkUtils_ValidateInvalidJoinCode_ReturnsFalse() {
        // Short
        assertThat(NetworkUtils.isValidJoinCode("256")).isFalse();
        assertThat(NetworkUtils.isValidJoinCode("0000")).isFalse();
        assertThat(NetworkUtils.isValidJoinCode("0001")).isFalse();
        assertThat(NetworkUtils.isValidJoinCode("-2")).isFalse();

        // Medium
        assertThat(NetworkUtils.isValidJoinCode("256.1")).isFalse();
        assertThat(NetworkUtils.isValidJoinCode("1.400")).isFalse();
        assertThat(NetworkUtils.isValidJoinCode("00300.1")).isFalse();
        assertThat(NetworkUtils.isValidJoinCode("1.23.1")).isFalse();
    }

    @Test
    public void networkUtils_ValidateValidIpAddress_ReturnsTrue() {
        assertThat(NetworkUtils.isValidJoinCode("123.123.123.123")).isTrue();
        assertThat(NetworkUtils.isValidJoinCode("20.24.255.103")).isTrue();
        assertThat(NetworkUtils.isValidJoinCode("0.0.0.0")).isTrue();
        assertThat(NetworkUtils.isValidJoinCode("192.168.0.1")).isTrue();
    }

    @Test
    public void networkUtils_ValidateInvalidIpAddress_ReturnsFalse() {
        assertThat(NetworkUtils.isValidJoinCode("123.123.123..123")).isFalse();
        assertThat(NetworkUtils.isValidJoinCode("20.24.400.103")).isFalse();
        assertThat(NetworkUtils.isValidJoinCode("0.0.0.0.1")).isFalse();
        assertThat(NetworkUtils.isValidJoinCode("192.168.0.1.")).isFalse();
        assertThat(NetworkUtils.isValidJoinCode("192.168.0")).isFalse();
    }
}
