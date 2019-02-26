package com.texasgamer.zephyr;

import com.texasgamer.zephyr.util.NetworkUtils;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class JoinCodeTests {
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
