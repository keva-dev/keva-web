---
home: true
heroImage: https://i.imgur.com/ErsKxIR.png
tagline: Lightweight Java web framework for rapid development in the API era
actionText: Quick Start →
actionLink: /guide/
features:
- title: Expressive
  details: The APIs is very simple and expressive, just read the guide and start building things in no time
- title: Lightweight
  details: No JavaEE, needn't learn Servlet, just use the APIs, no Servlet container, build and start are just in milliseconds
- title: Performant
  details: Blazing fast performance due to the simplicity nature of the framework, no need to learn the performance tricks
footer: Apache-2.0 Licensed | Copyright © 2021 Keva Team
---

<p style="text-align: right; max-width: 960px; margin: auto;">
<img src="https://img.shields.io/tokei/lines/github/keva-dev/keva-web?style=flat-square" alt="Lines of code">
<img src="https://img.shields.io/github/license/keva-dev/keva-web?style=flat-square" alt="GitHub">
</p>

<p style="max-width: 960px; margin: 1rem auto auto; text-align: center;">You have your RESTful API server
 ready to serve
 JSON in
 less
 than ten
 lines of code</p>

 ```java
import dev.keva.web.core.*;

public class Main {
    public static void main(String[] args) {
        HttpServer.builder().port(1234).build()
               .get("/hello", context -> 
                       HttpResponse.okJson(new Hello("world")))
               .run();
   }
}
```

<div style="text-align: center;">
    <svg style="transform: rotate(90deg); width: 1.5rem; height: 1.5rem;" aria-hidden="true" focusable="false" data-prefix="fas" data-icon="arrow-right" class="svg-inline--fa fa-arrow-right fa-w-14 " role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"><path fill="currentColor" d="M190.5 66.9l22.2-22.2c9.4-9.4 24.6-9.4 33.9 0L441 239c9.4 9.4 9.4 24.6 0 33.9L246.6 467.3c-9.4 9.4-24.6 9.4-33.9 0l-22.2-22.2c-9.5-9.5-9.3-25 .4-34.3L311.4 296H24c-13.3 0-24-10.7-24-24v-32c0-13.3 10.7-24 24-24h287.4L190.9 101.2c-9.8-9.3-10-24.8-.4-34.3z"></path></svg>
</div>

<div style="text-align: center;">
    <img src="https://i.imgur.com/feIt8al.png" style="max-width: 100%" />
</div>
