// ==UserScript==
// @name        MishasaminsMinionBots
// @namespace   MishasaminsMinionBots
// @include     http://agar.io/
// @include     http://agar.io/index.html
// @include     http://agar.io/?ip=*
// @include     https://agar.io/
// @include     https://agar.io/index.html
// @include     https://agar.io/?ip=*
// @version     1
// @grant       none
// @author      Mikhail Samin
// ==/UserScript==
/*
Copyright Mikhail Samin, 2015.
Only for non-commercial use.

The MIT License (MIT)

Copyright (c) 2015 Mikhail Samin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.


If you change this code, please, write to mishasamin@ya.ru (I want to update code when agar.io change).
If you copy or changing the code, this comment should remain unchanged.*/

    if ("undefined" == typeof hasRun) {
        hasRun = !0, window.stop(), document.documentElement.innerHTML = "";
        var e = new XMLHttpRequest;
        e.open("GET", "http://agar.io/html/index.html", !0), e.onload = function() {document.open(), document.write(this.responseText.replace(/\=\"\.\//g, '="http://agar.io/html/')), document.close()}, e.send()
    }
