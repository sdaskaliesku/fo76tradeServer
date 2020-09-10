package com.manson.fo76.helper

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*
import java.util.function.Function

@Component
class JwtTokenUtil : Serializable {
    @Value("#{systemProperties['jwt.secret']}")
    private val secret: String? = null
    fun getUsernameFromToken(token: String?): String {
        return getClaimFromToken(token) { obj: Claims -> obj.subject }
    }

    fun getIssuedAtDateFromToken(token: String?): Date {
        return getClaimFromToken(token) { obj: Claims -> obj.issuedAt }
    }

    fun getExpirationDateFromToken(token: String?): Date {
        return getClaimFromToken(token) { obj: Claims -> obj.expiration }
    }

    fun <T> getClaimFromToken(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }

    private fun getAllClaimsFromToken(token: String?): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
    }

    private fun isTokenExpired(token: String?): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    private fun ignoreTokenExpiration(token: String?): Boolean {
        // here you specify tokens, for that the expiration is ignored
        return false
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = HashMap()
        return doGenerateToken(claims, userDetails.username)
    }

    private fun doGenerateToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact()
    }

    fun canTokenBeRefreshed(token: String?): Boolean {
        return !isTokenExpired(token) || ignoreTokenExpiration(token)
    }

    fun validateToken(token: String?, userDetails: UserDetails?): Boolean {
        val username = getUsernameFromToken(token)
        return username == userDetails!!.username && !isTokenExpired(token)
    }

    companion object {
        const val JWT_TOKEN_VALIDITY = 5 * 60 * 60.toLong()
        private const val serialVersionUID = -2550185165626007488L
    }
}