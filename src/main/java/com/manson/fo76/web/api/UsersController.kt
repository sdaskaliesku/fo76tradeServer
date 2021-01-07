package com.manson.fo76.web.api

import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@RestController
@CrossOrigin
@ApiIgnore
@RequestMapping(value = ["/users"])
class UsersController {
//@Autowired constructor(private val authenticationManager: AuthenticationManager, private val jwtTokenUtil: JwtTokenUtil,
//                                             private val userService: UserService, private val itemService: ItemService) {
//    @PostMapping(value = ["/create"], consumes = ["application/json"], produces = ["application/json"])
//    fun createNewUser(@RequestBody user: User): User? {
//        return userService.createNewUser(user)
//    }
//
//    @PostMapping(value = ["/forgot"], consumes = ["application/json"], produces = ["application/json"])
//    fun forgot(@RequestBody user: User): User? {
//        return userService.findByIdOrName(user)
//    }
//
//    @PostMapping(value = ["/token"], consumes = ["application/json"], produces = ["application/json"])
//    fun token(@RequestBody token: String?): User? {
//        val username = jwtTokenUtil.getUsernameFromToken(token)
//        return userService.findByIdOrName(username)
//    }
//
//    @GetMapping(value = ["/findAll"], produces = ["application/json"])
//    fun findAll(): List<User?>? {
//        return userService.findAll()
//    }
//
//    @GetMapping(value = ["/findByIdOrName"], produces = ["application/json"])
//    fun findByIdOrName(idOrName: String?): User? {
//        return userService.findByIdOrName(idOrName)
//    }
//
//    @DeleteMapping(value = ["/delete"], consumes = ["application/json"], produces = ["application/json"])
//    fun delete(@RequestBody user: User): Boolean {
//        val userInDb = userService.delete(user)
//        return if (Objects.nonNull(userInDb)) {
//            itemService.deleteAllByOwnerId(userInDb!!.id)
//        } else false
//    }
//
//    @PostMapping(value = ["/authenticate"], consumes = ["application/json"], produces = ["application/json"])
//    fun generateAuthenticationToken(@RequestBody user: User): ResponseEntity<*> {
//        val userDetails = userService.loadUser(user)
//                ?: return ResponseEntity.status(HttpStatus.FORBIDDEN).build<Any>()
//        authenticate(user.name, user.password)
//        val token = jwtTokenUtil.generateToken(userDetails)
//        return ResponseEntity.ok(JwtResponse(token))
//    }
//
//    private fun authenticate(username: String?, password: String?) {
//        try {
//            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
}