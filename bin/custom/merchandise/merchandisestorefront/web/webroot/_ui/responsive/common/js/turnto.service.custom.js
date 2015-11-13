var turnToConfig = {
        siteKey: "2qtC5sJ5gVYcfvesite",
        setupType: "dynamicEmbed",
        embedCommentCapture: true,
        postPurchaseFlow: true,
        reviewsSetupType: "dynamicEmbed",
        recentComments: {
            height: "400px",
            width: "410px",
            category: "Stuff",
            // low or high
            frequency: "high",
            // Only used when frequency = low
            timeWindowInHours: 48,
            // crawlUp, zoomUp, none
            animationType: "none",
            animationFreqInMs: 5000
            // Optional: Change "What's this" copy
            , whatsThisTooltipText: "What's this custom text"
            , titleMaxLength: 60
            , nameMaxLength: 20
            // Optional: The URL of the full page checkout chatter
            //,fullPageUrl: ""
            // Optional: Change header copy
            //,header: "My custom header here"
        },
        fullComments: {
            height: "1000px",
            width: "1000px"
            //,category: "movies"
            , titleMaxLength: 60
            , nameMaxLength: 20
            , boxWidth: "212px"
            , limit: 20

            // "centered" or "justified"
            , layoutMode: "centered"

            // Always used for vertical spacing.
            // For horizontal spacing:
            //  If layoutMode == "centered" then this is the exact spacing between boxes
            //  If layoutMode == "justified" this value is used as the minimum spacing
            , spacing: "20px"
        }
    },
    TurnToItemSku = getSKU();

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

function getSKU(){
    val = window.location.href;
    durty_id = val.substr(val.indexOf("/p/") + 3);
    return  durty_id.split("?")[0];
}