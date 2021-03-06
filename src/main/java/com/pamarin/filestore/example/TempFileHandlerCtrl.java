/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore.example;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.pamarin.filestore.FileHandlerAdapter;
import com.pamarin.filestore.FileUploader;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2018/03/28
 */
@Controller
@RequestMapping(TempAccessPathFileRequestConverter.CONTEXT_PATH)
public class TempFileHandlerCtrl extends FileHandlerAdapter {

    private static final String SHARE_SECRET_KEY = "secret";

    @Autowired
    private TempFileUploader fileUploader;

    @Override
    protected FileUploader getFileUploader() {
        return fileUploader;
    }

    @Override
    protected String getUserId(HttpServletRequest httpReq) {
        return "1";//TODO : may be get value from SecurityContext
    }

    private Algorithm getAlgorithm() throws IllegalArgumentException, UnsupportedEncodingException {
        return Algorithm.HMAC256(SHARE_SECRET_KEY);
    }

    @Override
    public String signGrantToken(String userId) {
        try {
            return JWT.create()
                    .withIssuer(userId)
                    .sign(getAlgorithm());
        } catch (UnsupportedEncodingException | JWTCreationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String verifyGrantToken(String token) {
        try {
            return JWT.require(getAlgorithm())
                    .build()
                    .verify(token)
                    .getIssuer();
        } catch (UnsupportedEncodingException | JWTCreationException ex) {
            throw new RuntimeException(ex);
        }
    }

}
