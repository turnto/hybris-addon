var turnToConfig = {
    siteKey: "2qtC5sJ5gVYcfvesite"
};

(function () {
    var tt = document.createElement('script');
    tt.type = 'text/javascript';
    tt.async = true;
    tt.src = document.location.protocol + "//static.www.turnto.com/traServer4_2/trajs/" + turnToConfig.siteKey + "/tra.js";
    var s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(tt, s);
})();