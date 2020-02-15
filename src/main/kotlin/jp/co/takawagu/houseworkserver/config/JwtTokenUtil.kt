package jp.co.takawagu.houseworkserver.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jp.co.takawagu.houseworkserver.model.User
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@ConfigurationProperties(prefix = "signing")
@ConstructorBinding
class SigningKey(val key: String)

@Component
class JwtTokenUtil(private val signingKey: SigningKey) {
    companion object {
        const val ACCESS_TOKEN_VALIDITY_SECONDS = 30*60
    }

    fun getUserNameFromToken(token: String): String = getClaimFromToken(token, Claims::getSubject)

    fun getExpirationDateFromToken(token: String): Date = getClaimFromToken(token, Claims::getExpiration)

    fun isTokenExpired(token: String) : Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    fun <R> getClaimFromToken(token: String, claimsResolver: (Claims) -> R): R {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver(claims)
    }

    fun getAllClaimsFromToken(token: String): Claims =
            Jwts.parserBuilder()
                    .setSigningKey(signingKey.key)
                    .build()
                    .parseClaimsJws(token)
                    .body

    fun generateToken(user: User): String {
        return Jwts.builder()
                .setSubject(user.userName)
                .signWith(SignatureAlgorithm.HS256, signingKey.key)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + 1000*ACCESS_TOKEN_VALIDITY_SECONDS))
                .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val userName = getUserNameFromToken(token)
        return userName == userDetails.username && !isTokenExpired(token)
    }
}