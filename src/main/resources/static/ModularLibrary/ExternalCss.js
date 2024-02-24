import { CSSProcessor } from './CSSProcessor.js';


export class ExternalCss {

    constructor(suffix) {
        this.suffix = suffix;
        this.cssProcessor = new CSSProcessor(suffix);
    }

    modifyExternalStylesheet(url) {
        // Return the fetch promise
        return fetch(url)
            .then(response => response.text())
            .then(cssText => {
                // Modify the CSS text
                const modifiedCss = this.modifyCssText(cssText);
                // console.log(modifiedCss);
                return modifiedCss;
            })
            .catch(error => console.error('Error modifying external stylesheet:', error));
    }


    // Modify CSS text and return the modified version.
    modifyCssText(cssText) {
        // Create a temporary style element to parse CSS.
        const style = document.createElement('style');
        document.head.appendChild(style);
        style.textContent = cssText;
        // Process the CSS rules and remove the temporary style element.
        const modifiedCss = this.cssProcessor.processCssRules(style.sheet.cssRules);
        // console.log(modifiedCss);
        style.remove();
        return modifiedCss;
    }
}