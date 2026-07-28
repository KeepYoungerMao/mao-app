package com.mao.extension

import com.mao.config.JwtConfig
import com.mao.entity.ErrorCode
import com.mao.ex.AppException
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.interfaces.RSAPrivateKey
import java.time.Instant
import java.util.*

@Service
class JwtService(
    private val jwtConfig: JwtConfig,
    private val rsaKey: RSAKey
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun getPublicKey(): String {
        val encode = rsaKey.toRSAPublicKey().encoded
        return Base64.getEncoder().encodeToString(encode)
    }

    fun getPrivateKey(): RSAPrivateKey = rsaKey.toRSAPrivateKey()

    fun generateAccessToken(user: UserDetails): String {
        val now = Instant.now()
        val claims = JWTClaimsSet.Builder()
            .subject(user.username)
            .issueTime(Date.from(now))
            .expirationTime(Date.from(now.plusMillis(jwtConfig.accessTokenExpiration)))
            .claim("type", "access")
            .claim("roles", user.authorities.map { it.authority }.toSet())
            .issuer("auth-server")
            .build()
        return sign(claims)
    }

    fun generateRefreshToken(user: UserDetails): String {
        val now = Instant.now()
        val claims = JWTClaimsSet.Builder()
            .subject(user.username)
            .issueTime(Date.from(now))
            .expirationTime(Date.from(now.plusMillis(jwtConfig.refreshTokenExpiration)))
            .claim("type", "refresh")
            .issuer("auth-server")
            .build()
        return sign(claims)
    }

    fun validateToken(token: String): JWTClaimsSet {
        return try {
            val signedJWT = SignedJWT.parse(token)
            val verifier = RSASSAVerifier(rsaKey.toPublicJWK())
            if (signedJWT.verify(verifier)) {
                val claims = signedJWT.jwtClaimsSet
                if (claims.expirationTime.after(Date())) {
                    claims
                } else {
                    throw AppException(ErrorCode.TOKEN_EXPIRED)
                }
            } else {
                throw AppException(ErrorCode.INVALID_TOKEN)
            }
        } catch (e: AppException) {
            throw e
        } catch (e: Exception) {
            log.error("验证token时失败: ", e)
            throw AppException(ErrorCode.INVALID_TOKEN)
        }
    }

    private fun sign(claims: JWTClaimsSet): String {
        val signer = RSASSASigner(rsaKey)
        val signedJWT = SignedJWT(JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKey.keyID).build(), claims)
        signedJWT.sign(signer)
        return signedJWT.serialize()
    }

}