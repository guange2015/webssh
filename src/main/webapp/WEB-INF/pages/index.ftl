<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>webssh</title>
    <link rel='shortcut icon' type='image/x-icon' href='/static/image/favicon.ico'/>
    <link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.6.0/pure-min.css">
    <link href="/static/css/tooltip.css" rel="stylesheet" type="text/css"/>
    <link href="/static/css/main.css" rel="stylesheet" type="text/css"/>
    <link href="/static/css/xterm.css" rel="stylesheet" type="text/css"/>
</head>
<header id="head" class="aside">Login SSH</header>
<body>
<div id="main" align="center">
    <form id="form" name="form" class="pure-form pure-form-stacked">
        <fieldset>
            <div class="pure-item">
                <label for="host">Host</label>
                <input id="host" name="host" type="text" placeholder="Host">
            </div>
            <div class="pure-item">
                <label for="port">Port</label>
                <input id="port" name="port" type="text" placeholder="Port">
            </div>
            <div class="pure-item">
                <label for="username">Username</label>
                <input id="username" name="username" type="text" placeholder="Username">
            </div>
            <div class="pure-item">
                <label>Type</label>
                <div id="ratio-group">
                    <label for="password" class="pure-radio">
                        <input id="password" type="radio" name="ispwd" value="true" checked>
                        Password
                    </label>

                    <label for="primerykey" class="pure-radio">
                        <input id="primerykey" type="radio" name="ispwd" value="false">
                        Primary Key
                    </label>
                </div>
            </div>
            <div class="pure-item">
                <label for="secret">Secret</label>
                <input id="secret" name="secret" type="password"
                       placeholder="Password or Private Key">
            </div>
            <label for="remember" class="pure-checkbox">
                <input id="remember" type="checkbox"> Remember me
            </label>
            <button type="button" id="connect-btn" class="pure-button pure-button-primary">Connect</button>
        </fieldset>
    </form>
</div>
<div id="term" align="center"></div>
<script src="/static/js/jquery.min.js"></script>
<script src="/static/js/xterm.js"></script>
<script src="/static/js/base64.js"></script>
<script src="/static/js/ws.js"></script>
<script src="/static/js/formvalid.js"></script>
<script src="/static/js/main.js"></script>
<script type="application/javascript">
    $(function () {
        $("#connect-btn").on('click', function(){
            connect();
        });

        $("#form").valid([
            {name: "host", type: "ip"},
            {name: "port", type: "port"},
            {name: "username", type: "username"},
            //{name: "password", type: "password"},
        ]);
    })
</script>
</body>
</html>