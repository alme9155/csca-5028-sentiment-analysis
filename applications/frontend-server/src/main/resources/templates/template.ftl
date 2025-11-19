<#macro noauthentication title="Welcome">
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name=viewport content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="/static/styles/reset.css">
        <link rel="stylesheet" href="/static/styles/style.css">
        <link rel="stylesheet" href="/static/styles/results.css">        
        <title>${title}</title>
    </head>
    <body>
    <header>
        <div class="container">
            <h1>AI-Powered Movie Rating System</h1>
            <h3>This project aims to convert user-submitted movie reviews into rating scores using sentiment analysis from a Natural Language Processor. </h3>
        </div>
    </header>
    <section class="callout">
        <div class="container">
            <span class="branded">CSCA-5028</span>
            <span class="branded">Applications of Software Architecture for Big Data</span>
        </div>
    </section>
    <main>
        <#nested>
    </main>
    <footer>
        <div class="container">
            <script>document.write("©" + new Date().getFullYear());</script>
            Alex M (alme9155). All rights reserved.
        </div>
    </footer>
    </body>
    </html>
</#macro>
