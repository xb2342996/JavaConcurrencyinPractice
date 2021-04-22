package com.xxbb.jcip.threadsafety.safe;

import com.xxbb.jcip.annotation.GuardBy;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CachedFactorizer extends GenericServlet {
    @GuardBy("this")
    private BigInteger lastNumber;
    @GuardBy("this")
    private BigInteger[] lastFactors;

    @GuardBy("this")
    private long hits;
    @GuardBy("this")
    private long cacheHits;

    public synchronized long getHits() {
        return hits;
    }

    public synchronized double getCacheHitRatio() {
        return (double) cacheHits / (double)hits;
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        BigInteger i = extractFromRequest(servletRequest);
        BigInteger[] factors = null;
        synchronized (this) {
            ++ hits;
            if (i.equals(lastNumber)) {
                ++ cacheHits;
                factors = lastFactors.clone();
            }
        }

        if (factors == null) {
            factors = factor(i);
            synchronized (this) {
                lastNumber = i;
                lastFactors = factors.clone();
            }
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

