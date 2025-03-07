package city.smartb.iris.vc.signer;

import city.smartb.iris.crypto.rsa.RSAKeyPairReader;
import city.smartb.iris.crypto.rsa.exception.InvalidRsaKeyException;
import city.smartb.iris.crypto.rsa.signer.Signer;
import city.smartb.iris.crypto.rsa.verifier.Verifier;
import city.smartb.iris.ldproof.LdProofBuilder;
import city.smartb.iris.vc.VerifiableCredential;
import city.smartb.iris.vc.VerifiableCredentialBuilder;
import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class VCSignTest {

    private VCVerifier vcVerifier = new VCVerifier();
    private VCSign vcSign = new VCSign();

    @Test
    void sign() throws GeneralSecurityException, InvalidRsaKeyException {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("name", "smartb");

        VerifiableCredentialBuilder vcBuild = VerifiableCredentialBuilder
                .create()
                .withContextDefault()
                .withId("477599e2-eab4-4cd8-b4ab-75aad8a21f2e")
                .withIssuanceDate("2020-05-25T11:37:24.293")
                .withIssuer("UnitTest")
                .withCredentialSubject(claims);

        LdProofBuilder proofBuilder = LdProofBuilder.builder()
                .withChallenge("Chalenges")
                .withCreated(LocalDateTime.parse("2020-05-25T11:37:24.293"))
                .withDomain("smartb.city")
                .withProofPurpose("ProofPurpose")
                .withVerificationMethod("VerificationMethod");

        KeyPair pair = RSAKeyPairReader.loadKeyPair("userAgentUnitTest");
        Signer signer = Signer.rs256Signer((RSAPrivateKey) pair.getPrivate());

        VerifiableCredential cred = vcSign.sign(vcBuild, proofBuilder, signer);

        Assertions.assertThat(cred.getProof()).isNotNull();
        Assertions.assertThat(cred.getProof().getJws()).isEqualTo("eyJiNjQiOmZhbHNlLCJjcml0IjpbImI2NCJdLCJhbGciOiJSUzI1NiJ9..QT2nS9mUkFIQnCHM5EJItJ5ZvlUZKoMQBeydJ9YtXypy228vrloDmAZr1fCeL1QEX8dnqfQrQFfHFeGdZIuD1pIsnMUdAbIBdlwCE_Dax8BzCRqXWaiCz6nsz5Bjbsbpc78DvUqF3ass28vkvZrhMYzoDtqjtQz4LMjRHBe0eAosp37pQHuo7v6hilPCWz82mzO2A7rI16AWDO0d9DUIkSWOShGQO22mA5UC9zXrTs3CvY0zNR4rJB_I7Akh75HjQpgOSALrkzy5yE8OCx09QGN69xT46qY7nsdG8-KWT6dThwjhslXIVXUvowZPld_oIOGUfpN8-bp_LxDnX7ZApA");
        Assertions.assertThat(cred.getProof().getChallenge()).isEqualTo("Chalenges");
        Assertions.assertThat(cred.getProof().getCreated()).isEqualTo("2020-05-25T11:37:24.293");
        Assertions.assertThat(cred.getProof().getDomain()).isEqualTo("smartb.city");
        Assertions.assertThat(cred.getProof().getProofPurpose()).isEqualTo("ProofPurpose");
        Assertions.assertThat(cred.getProof().getType()).isEqualTo("RsaSignature2018");
        Assertions.assertThat(cred.getProof().getVerificationMethod()).isEqualTo("VerificationMethod");

    }


    @Test
    void verify() throws GeneralSecurityException, InvalidRsaKeyException {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("name", "smartb");

        VerifiableCredentialBuilder vcBuild = VerifiableCredentialBuilder
                .create()
                .withId("477599e2-eab4-4cd8-b4ab-75aad8a21f2e")
                .withIssuanceDate("2020-05-25T11:37:24.293")
                .withIssuer("UnitTest")
                .withCredentialSubject(claims);

        LdProofBuilder proofBuilder = LdProofBuilder.builder()
                .withChallenge("Chalenges")
                .withCreated(LocalDateTime.parse("2020-05-25T11:37:24.293"))
                .withDomain("smartb.city")
                .withProofPurpose("ProofPurpose")
                .withVerificationMethod("VerificationMethod");

        KeyPair pair = RSAKeyPairReader.loadKeyPair("userAgentUnitTest");
        Signer signer = Signer.rs256Signer((RSAPrivateKey) pair.getPrivate());

        VerifiableCredential cred = vcSign.sign(vcBuild, proofBuilder, signer);

        Verifier verifier = Verifier.rs256Verifier((RSAPublicKey) pair.getPublic());
        Boolean isValid = vcVerifier.verify(cred, verifier);

        Assertions.assertThat(isValid).isTrue();
    }


    @Test
    void verifyVCContainsCustomValue() throws GeneralSecurityException, InvalidRsaKeyException {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("name", "smartb");

        VerifiableCredentialBuilder vcBuild = VerifiableCredentialBuilder
                .create()
                .withId("477599e2-eab4-4cd8-b4ab-75aad8a21f2e")
                .withIssuanceDate("2020-05-25T11:37:24.293")
                .withIssuer("UnitTest")
                .withCredentialSubject(claims)
                .with("custom", VCRef.list());

        LdProofBuilder proofBuilder = LdProofBuilder.builder()
                .withChallenge("Chalenges")
                .withCreated(LocalDateTime.parse("2020-05-25T11:37:24.293"))
                .withDomain("smartb.city")
                .withProofPurpose("ProofPurpose")
                .withVerificationMethod("VerificationMethod");

        KeyPair pair = RSAKeyPairReader.loadKeyPair("userAgentUnitTest");
        Signer signer = Signer.rs256Signer((RSAPrivateKey) pair.getPrivate());

        VerifiableCredential cred = vcSign.sign(vcBuild, proofBuilder, signer);

        Verifier verifier = Verifier.rs256Verifier((RSAPublicKey) pair.getPublic());
        Boolean isValid = vcVerifier.verify(cred, verifier);

        Assertions.assertThat(isValid).isTrue();
        Assertions.assertThat(cred.get("custom").asListObjects(VCRef.class)).hasSize(2);
    }

    public static class VCRef {
        public static List<VCRef> list() {
            return ImmutableList.of(
                    new VCRef().setId("1").setJws("jws1"),
                    new VCRef().setId("2").setJws("jws2")
            );
        }

        private String jws;
        private String id;

        public String getJws() {
            return jws;
        }

        public VCRef setJws(String jws) {
            this.jws = jws;
            return this;
        }

        public String getId() {
            return id;
        }

        public VCRef setId(String id) {
            this.id = id;
            return this;
        }
    }
}