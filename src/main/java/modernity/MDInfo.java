/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity;

import net.rgsw.UselessOperationException;

import java.io.FileInputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public final class MDInfo {
    public static final String MODID = "modernity";
    public static final String VERSION = DynamicConstants.VERSION;
    public static final String VERSION_NAME = DynamicConstants.VERSION_NAME;
    public static final String SHA1 = DynamicConstants.SHA1;
    public static final boolean IDE = DynamicConstants.IDE;
    public static final boolean SIGNED = DynamicConstants.SIGNED;

    private MDInfo() {
        throw new UselessOperationException( "No MDInfo instances for you!" );
    }

    public static PublicKey getPublicKey( String filename ) throws Exception {

//        URL url = MDInfo.class.getClassLoader().getResource( filename );
//        byte[] keyBytes = Files.readAllBytes( Paths.get( Objects.requireNonNull( url ).toURI() ) );
//
//        X509EncodedKeySpec spec = new X509EncodedKeySpec( keyBytes );
        FileInputStream fin = new FileInputStream( filename );
        CertificateFactory f = CertificateFactory.getInstance( "X.509" );
        X509Certificate cert = (X509Certificate) f.generateCertificate( fin );
        return cert.getPublicKey();
    }

    // Bunch of constants gradle would replace the uses of
    private static final class DynamicConstants {
        static final boolean IDE = true;
        static final boolean SIGNED = false;
        static final String VERSION = "UNVERSIONED";
        static final String VERSION_NAME = "The Gradle-Did-Not-Give-Me-A-Version-Version";
        static final String SHA1 = "NO:FI:NG:ER:PR:IN:TA:VA:IL:AB:LE";
    }
}
