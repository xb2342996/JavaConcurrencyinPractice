package com.xxbb.jcip.sharingobjects.safe;

import com.xxbb.jcip.annotation.ThreadSafe;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.math.BigInteger;

@ThreadSafe
public class VolatileCachedFactorizer extends GenericServlet {

    private volatile OneValueCache oneValueCache = new OneValueCache(null, null);

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        BigInteger i = extractFromRequest(servletRequest);
        BigInteger[] factors = oneValueCache.getFactors(i);

        if (factors == null) {
            factors = factor(i);
            oneValueCache = new OneValueCache(i, factors);
        }
        encodeIntoResponse(servletResponse, factors);
    }

    public void encodeIntoResponse(ServletResponse resp, BigInteger[] i) {

    }

    public BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    public BigInteger[] factor(BigInteger i) {
        return new BigInteger[] {i};
    }
}
