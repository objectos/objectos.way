/*
 * Copyright (C) 2023 Objectos Software LTDA.
 *
 * This file is part of the Objectos UI project.
 *
 * Objectos UI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Objectos UI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Objectos UI.  If not, see <https://www.gnu.org/licenses/>.
 */
package testing.site.web;

import static org.testng.Assert.assertEquals;
import static testing.zite.TestingTestingSite.serverExchange;

import java.io.IOException;
import objectos.http.WaySession;
import org.testng.annotations.Test;
import testing.zite.TestingTestingSite;

public class LoginTest {

  @Test(description = """
  It should render the login page (step 1) without validation errors
  """)
  public void testCase01() throws IOException {
    Login login;
    login = new Login(TestingTestingSite.INJECTOR);

    assertEquals(
        serverExchange("""
        GET /login HTTP/1.1\r
        Host: www.example.com\r
        \r
        """, login::handle),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/html; charset=utf-8\r
        Content-Length: 1548\r
        Set-Cookie: OBJECTOSWAY=a86886a5d2978142da2d8cf378ebc83c; Path=/\r
        \r
        <!DOCTYPE html>
        <html>
        <head>
        <title>Login Page</title>
        </head>
        <body class="bg-gray-100" data-frame="root" data-frame-value="login">
        <div class="mx-4 flex h-screen flex-col items-center sm:mx-auto">
        <form class="my-auto w-full bg-white sm:w-auto" id="login-form" action="/login" method="post" data-frame="form" data-frame-value="1"><input name="step" type="hidden" value="one">
        <div class="mb-1 p-4 pb-0 text-2xl leading-none tracking-tighter">Log in</div>
        <div class="mb-12 px-4 text-gray-700 text-sm leading-none tracking-tighter">Don't have an account? <a class="text-blue-700" href="#">Create a new one</a></div>
        <div class="relative mb-6"><label class="absolute top-4 left-4 text-xs text-gray-700" for="login">Email address</label><input class="w-full border-y border-t-gray-300 border-b-gray-400 px-4 pt-8 pb-3.5 text-sm" id="login" name="login" type="text" placeholder="user@example.com" required></div>
        <div class="flex mb-10 pl-4"><input id="remember" name="remember" type="checkbox"><label class="ml-3 text-sm text-gray-700" for="remember">Remember me</label></div>
        <div class="flex">
        <div class="hidden sm:block sm:w-60">&nbsp;</div>
        <button class="flex justify-between h-16 w-full bg-blue-600 px-4 pt-4 text-left text-sm text-white sm:w-60" type="submit"><span>Continue</span><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 32 32" class="fill-current">
        <path d="M28 16 21 9 19.586 10.414 24.172 15 4 15 4 17 24.172 17 19.586 21.586 21 23 28 16z"></path>
        </svg></button></div>
        </form>
        </div>
        </body>
        </html>
        """
    );
  }

  @Test(description = """

  """)
  public void testCase02() {
    Login login;
    login = new Login(TestingTestingSite.INJECTOR);

    WaySession session;
    session = new WaySession("a86886a5d2978142da2d8cf378ebc83c");

    assertEquals(
        serverExchange("""
        POST /login HTTP/1.1\r
        Host: www.example.com\r
        Content-Type: application/x-www-form-urlencoded\r
        Content-Length: 20\r
        Cookie: OBJECTOSWAY=a86886a5d2978142da2d8cf378ebc83c\r
        \r
        step=one&login=admin""", login::handle, store -> store.add(session)),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/html; charset=utf-8\r
        Content-Length: 1449\r
        \r
        <!DOCTYPE html>
        <html>
        <head>
        <title>Login Page</title>
        </head>
        <body class="bg-gray-100" data-frame="root" data-frame-value="login">
        <div class="mx-4 flex h-screen flex-col items-center sm:mx-auto">
        <form class="my-auto w-full bg-white sm:w-auto" id="login-form" action="/login" method="post" data-frame="form" data-frame-value="2"><input name="step" type="hidden" value="two"><input name="login" type="hidden" value="admin">
        <div class="mb-1 p-4 pb-0 text-2xl leading-none tracking-tighter">Log In</div>
        <div class="mb-12 px-4 text-gray-700 text-sm leading-none tracking-tighter">Logging in as admin&nbsp;<a class="text-blue-700" href="#">Not you?</a></div>
        <div class="relative mb-6"><label class="absolute top-4 left-4 text-xs text-gray-700" for="password">Password</label><input class="w-full border-y border-t-gray-300 border-b-gray-400 pt-8 px-4 pb-3.5 text-sm tracking-widest" id="password" name="password" type="password"></div>
        <div class="flex mb-10 pl-4">&nbsp;</div>
        <div class="flex">
        <div class="hidden sm:block sm:w-60">&nbsp;</div>
        <button class="flex justify-between h-16 w-full bg-blue-600 px-4 pt-4 text-left text-sm text-white sm:w-60" type="submit"><span>Log in</span><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 32 32" class="fill-current">
        <path d="M28 16 21 9 19.586 10.414 24.172 15 4 15 4 17 24.172 17 19.586 21.586 21 23 28 16z"></path>
        </svg></button></div>
        </form>
        </div>
        </body>
        </html>
        """
    );
  }

}