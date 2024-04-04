// JSRenderer.js

export class JSRenderer {
    constructor(suffix) {
        this.suffix = suffix;
    }

    // Method to add suffix to JS class names
    renderJsClasses(originalClassNames) {
        return originalClassNames.map(className => `${className}_${this.suffix}`);
    }
}
