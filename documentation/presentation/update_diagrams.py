"""After editing microservices.drawio in draw.io, run:  python update_diagrams.py
The script regenerates diagrams.js, so the HTML pages show the new version of the diagrams.
Also re-export the PNG images in img/ (File > Export as > PNG, one page at a time):
they are used as previews on the home page and as a fallback without an Internet connection."""
import json, pathlib

here = pathlib.Path(__file__).resolve().parent
xml = (here / "microservices.drawio").read_text(encoding="utf-8")
js = ("// Generated from microservices.drawio by update_diagrams.py — do not edit by hand.\n"
      "window.JOBBOARD_DRAWIO = " + json.dumps(xml, ensure_ascii=False) + ";\n")
(here / "diagrams.js").write_text(js, encoding="utf-8")
print("diagrams.js updated (%d pages)." % xml.count("<diagram "))
