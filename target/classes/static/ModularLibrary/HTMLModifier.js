export class HTMLModifier {
    constructor(suffix) {
        this.suffix = suffix;
    }

    // Modify HTML content
    modifyHtml(doc, containerSelector) {
        this.modifyElementClasses(doc);
        this.addTagNameClasses(doc);

        const container = document.querySelector(containerSelector);
        if (container) {
            container.innerHTML = doc.body.innerHTML;
        }
    }

    modifyElementClasses(doc) {
        const elements = doc.querySelectorAll('*');
        elements.forEach(element => {
            if (element.classList.length > 0) {

                const modifiedClasses = Array.from(element.classList).map(className => {
                    return `${className}_${this.suffix}`;
                }).join(' '); // Join the array into a space-separated string
                element.className = modifiedClasses;
            }
        });
    }


    addTagNameClasses(doc) {
        const elements = doc.querySelectorAll('*');
        elements.forEach(element => {
            const tagNameClass = `${element.tagName.toLowerCase()}_tag_${this.suffix}`;
            element.classList.add(tagNameClass);
        });
    }
}
