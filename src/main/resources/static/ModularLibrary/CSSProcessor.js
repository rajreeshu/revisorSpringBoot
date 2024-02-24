export class CSSProcessor {
    constructor(suffix) {
        this.suffix = suffix;
    }

    processStyleElements(doc) {
        const styleElements = doc.querySelectorAll('style');
        let modifiedCss = '';
        for (let sheet of styleElements) {
            try {
                if (sheet.sheet.cssRules) {
                    modifiedCss += this.processCssRules(sheet.sheet.cssRules);
                }
            } catch (e) {
                console.warn("Something went wrong while applying CSS Processing", e);
            }
        }
        return modifiedCss;
    }

    processCssRules(cssRules) {
        // console.log(cssRules);
        let modifiedCss = '';
        for (let rule of cssRules) {
            if (rule.type === CSSRule.STYLE_RULE) {
                rule.selectorText = this.addSuffixToSelector(rule.selectorText);
                modifiedCss += rule.cssText;
            } else if (rule.type === CSSRule.MEDIA_RULE) {
                modifiedCss += `@media ${rule.media.mediaText} { ${this.processCssRules(rule.cssRules)} }`;
            }
        }
        // console.log(modifiedCss);
        // setTimeout(()=>console.log("hello"),2000);
        return modifiedCss;
    }

    addSuffixToSelector(selector) {
        return selector.split(',').map(part =>
            part.trim().split(' ').map(subPart => {
                if (subPart.startsWith('.')) {
                    return `${subPart}_${this.suffix}`;
                } else if (/^[a-zA-Z0-9-]+$/.test(subPart)) {
                    return `.${subPart}_tag_${this.suffix}`;
                } else {
                    return subPart;
                }
            }).join(' ')
        ).join(', ');
    }

    injectModifiedCss(modifiedCss) {
        var styleNew = document.createElement('style');
        styleNew.textContent = modifiedCss;
        document.head.appendChild(styleNew);
    }
}
