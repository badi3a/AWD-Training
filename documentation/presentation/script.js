// JobBoard documentation — navigation + draw.io diagrams
// The diagrams come from diagrams.js (generated from microservices.drawio by update_diagrams.py).

// 1. "Burger" menu on small screens
document.addEventListener("DOMContentLoaded", function () {
    var burger = document.querySelector(".burger");
    var links = document.querySelector(".nav-links");
    if (burger && links) {
        burger.addEventListener("click", function () {
            var open = links.classList.toggle("nav-active");
            burger.setAttribute("aria-expanded", open ? "true" : "false");
        });
    }
});

// 2. draw.io viewer: each <div class="diagram" data-page="N"> shows page N of the file
(function () {
    var VIEWER = "https://viewer.diagrams.net/js/viewer-static.min.js";

    function offline() {
        document.querySelectorAll(".diagram").forEach(function (d) { d.classList.add("offline"); });
    }

    document.addEventListener("DOMContentLoaded", function () {
        var blocks = document.querySelectorAll(".diagram[data-page]");
        if (!blocks.length) return;
        if (typeof window.JOBBOARD_DRAWIO !== "string") { offline(); return; }

        blocks.forEach(function (block) {
            var cfg = {
                highlight: "#7E0C6E",
                nav: true,
                resize: true,
                page: parseInt(block.dataset.page, 10),
                toolbar: "pages zoom layers lightbox",
                edit: "_blank",
                xml: window.JOBBOARD_DRAWIO
            };
            var div = document.createElement("div");
            div.className = "mxgraph";
            div.setAttribute("data-mxgraph", JSON.stringify(cfg));
            block.insertBefore(div, block.firstChild);
        });

        var s = document.createElement("script");
        s.src = VIEWER;
        s.onload = function () {
            // The viewer renders .mxgraph elements by itself; run it again only if nothing was drawn
            setTimeout(function () {
                if (!document.querySelector(".diagram svg") && window.GraphViewer) {
                    window.GraphViewer.processElements();
                }
            }, 300);
        };
        s.onerror = offline; // no Internet: show the fallback PNG image
        document.body.appendChild(s);
    });
})();
