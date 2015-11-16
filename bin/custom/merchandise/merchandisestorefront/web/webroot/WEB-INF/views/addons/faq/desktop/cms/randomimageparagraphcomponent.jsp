<HTML>
<HEAD>
    sdfsdfdsfsdfsdf
    <script type="text/javascript">
        var turnToConfig = {
                    siteKey: "2qtC5sJ5gVYcfvesite",
                    setupType: "dynamicEmbed",
                    reviewsSetupType: "staticEmbed",
                    recentComments: {
                        height: "300px",
                        width: "310px",
                        // category: "movies",
                        // low or high
                        frequency: "high",
                        // Only used when frequency = low
                        timeWindowInHours: 48,
                        // crawlUp, zoomUp, none
                        animationType: "none",
                        animationFreqInMs: 5000
                        // Optional: Change "What's this" copy
                        ,whatsThisTooltipText: "What's this custom text"
                        ,titleMaxLength: 60
                        ,nameMaxLength: 20
                        // Optional: The URL of the full page checkout chatter
                        //,fullPageUrl: ""
                        // Optional: Change header copy
                        //,header: "My custom header here"
                    }
                },
                TurnToItemSku = "${productCode}";

        (function () {
            var tt = document.createElement('script');
            tt.type = 'text/javascript';
            tt.async = true;
            tt.src = document.location.protocol + "//static.www.turnto.com/traServer4_2/trajs/" + turnToConfig.siteKey + "/tra.js";
            var s = document.getElementsByTagName('script')[0];
            s.parentNode.insertBefore(tt, s);
        })();

        (function () {
                    var t = document.createElement('script');
                    t.type = 'text/javascript';
                    t.async = true;
                    t.src = document.location.protocol + "//static.www.turnto.com/sitedata/2qtC5sJ5gVYcfvesite/v4_2/" + TurnToItemSku + "/d/itemjs";
                    var s = document.getElementsByTagName('script')[0];
                    s.parentNode.insertBefore(t, s);
                })
        ();

    </script>
</HEAD>
<h1>Widget QA</h1>
<BODY>
<div id="TurnToRecentComments">вававы</div>
<%--<span class="TurnToItemInputTeaser"></span>--%>
<div id="TurnToContent"></div>
<div id="TurnToReviewsContent"></div>
<span class="TurnToReviewsTeaser"></span>
</BODY>
</HTML>

