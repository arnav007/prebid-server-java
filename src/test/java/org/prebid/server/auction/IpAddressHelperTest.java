package org.prebid.server.auction;

import org.junit.Before;
import org.junit.Test;
import org.prebid.server.auction.model.IpAddress;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class IpAddressHelperTest {

    private IpAddressHelper ipAddressHelper;

    @Before
    public void setUp() {
        ipAddressHelper = new IpAddressHelper(64, 56, asList("::1/128", "fc00::/7", "fe80::/10"));
    }

    @Test
    public void maskIpv6ShouldFillDiscardedBitsWithZero() {
        assertThat(ipAddressHelper.maskIpv6("1111:2222:3333:4444:5555:6666:7777:8888"))
                .isEqualTo("1111:2222:3333:4444::");
    }

    @Test
    public void maskIpv6ShouldReturnNullIfIpIsNotValid() {
        assertThat(ipAddressHelper.maskIpv6("abc")).isNull();
    }

    @Test
    public void anonymizeIpv6ShouldFillDiscardedBitsWithZero() {
        assertThat(ipAddressHelper.anonymizeIpv6("1111:2222:3333:4444:5555:6666:7777:8888"))
                .isEqualTo("1111:2222:3333:4400::");
    }

    @Test
    public void anonymizeIpv6ShouldReturnNullIfIpIsNotValid() {
        assertThat(ipAddressHelper.anonymizeIpv6("abc")).isNull();
    }

    @Test
    public void toIpAddressShouldReturnNullIfIpIsNotValid() {
        assertThat(ipAddressHelper.toIpAddress("abc")).isNull();
    }

    @Test
    public void toIpAddressShouldReturnNullIfIpIsV4AndLoopback() {
        assertThat(ipAddressHelper.toIpAddress("127.0.0.1")).isNull();
    }

    @Test
    public void toIpAddressShouldReturnNullIfIpIsV4AndLinkLocal() {
        assertThat(ipAddressHelper.toIpAddress("169.254.0.1")).isNull();
    }

    @Test
    public void toIpAddressShouldReturnNullIfIpIsV4AndSiteLocal() {
        assertThat(ipAddressHelper.toIpAddress("192.168.0.1")).isNull();
    }

    @Test
    public void toIpAddressShouldReturnNullIfIpIsV6AndLocal() {
        assertThat(ipAddressHelper.toIpAddress("fc00:0000:0000:0000:0000:0000:0000:0001")).isNull();
    }

    @Test
    public void toIpAddressShouldReturnIpV4Address() {
        assertThat(ipAddressHelper.toIpAddress("12.34.56.78")).isEqualTo(IpAddress.of("12.34.56.78", IpAddress.IP.v4));
    }

    @Test
    public void toIpAddressShouldReturnIpV6AddressMasked() {
        assertThat(ipAddressHelper.toIpAddress("2001:1db8:85a3:a5b7:0000:8a2e:0370:7334"))
                .isEqualTo(IpAddress.of("2001:1db8:85a3:a5b7::", IpAddress.IP.v6));
    }
}