package com.czetsuya;

import java.security.Principal;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/cognito")
@RequestScoped
public class CognitoResource {

	@Inject
	JsonWebToken jwt;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "Hello World!";
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("hello")
	public String helloWorld() {
		return "Hello EC2 x 8";
	}

	@GET()
	@Path("permit-all")
	@PermitAll
	@Produces(MediaType.TEXT_PLAIN)
	public String permitAll(@Context SecurityContext ctx) {
		Principal caller = ctx.getUserPrincipal();
		String name = caller == null ? "anonymous" : caller.getName();
		boolean hasJWT = jwt.getClaimNames() != null;
		String helloReply = String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s", name, ctx.isSecure(),
				ctx.getAuthenticationScheme(), hasJWT);
		return helloReply;
	}

	@GET()
	@Path("roles-allowed")
	@RolesAllowed({ "Creative" })
	@Produces(MediaType.TEXT_PLAIN)
	public String rolesAllowed(@Context SecurityContext ctx) {
		System.out.println("x: " + jwt.getGroups());
		System.out.println("" + jwt.getClaim("username"));
		System.out.println("" + jwt.getClaim("cognito:groups"));
		Principal caller = ctx.getUserPrincipal();
		String name = caller == null ? "anonymous" : caller.getName();
		boolean hasJWT = jwt.getClaimNames() != null;
		String helloReply = String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s", name, ctx.isSecure(),
				ctx.getAuthenticationScheme(), hasJWT);
		return helloReply;
	}
}
