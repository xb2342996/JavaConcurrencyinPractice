package com.xxbb.jcip.buildingblocks.safe;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.math.BigInteger;

public class Factorizer extends GenericServlet {

    private final Computable<BigInteger, BigInteger[]> c = new Computable<BigInteger, BigInteger[]>() {
        @Override
        public BigInteger[] compute(BigInteger arg) {
            return factor(arg);
        }
    };

    private final Computable<BigInteger, BigInteger[]> cache = new Memorizer<BigInteger, BigInteger[]>(c);

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        try {
            BigInteger i = extractFromRequest(servletRequest);
            encodeIntoResponse(servletResponse, cache.compute(i));
        } catch (InterruptedException e) {
            encodeError(servletResponse, "factorization interrupted");
        }
    }

    public void encodeError(ServletResponse resp, String message) {

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
