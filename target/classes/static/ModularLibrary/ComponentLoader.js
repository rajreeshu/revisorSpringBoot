export class ComponentLoader {
    constructor(suffix) {
        this.suffix = suffix;
    }

    loadComponent(url) {
        return fetch(url)
            .then(response => response.text())
            .then(html => {
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');
                return doc;
            })
            .catch(error => console.error('Error loading component:', error));
    }
}

