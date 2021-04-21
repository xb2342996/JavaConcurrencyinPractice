package com.xxbb.jcip.threadsafety.safe;

import com.xxbb.jcip.annotation.GuardBy;
import com.xxbb.jcip.annotation.ThreadSafe;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class SynchronizedFactorizer extends GenericServlet {
    @GuardBy("this")
    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<BigInteger>();
    @GuardBy("this")
    private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<BigInteger[]>();

    public synchronized void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        BigInteger i = extractFromRequest(servletRequest);
        if (i.equals(lastNumber.get())) {
            encodeIntoResponse(servletResponse, lastFactors.get());
        } else {
            BigInteger[] factors = factor(i);
            lastNumber.set(i);
            lastFactors.set(factors);
            encodeIntoResponse(servletResponse, factors);
        }
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
