document.addEventListener("DOMContentLoaded", function() {
    const applyForms = document.querySelectorAll(".apply-form");

    applyForms.forEach(form => {
        form.addEventListener("submit", function(event) {
            event.preventDefault();

            const formData = new FormData(this);
            const groupId = formData.get("groupId");
            const button = document.querySelector(`#apply-button-${groupId}`);

            fetch(this.action, {
                method: this.method,
                body: new URLSearchParams(formData)
            }).then(response => {
                if (response.ok) {
                    console.log("Запрос успешен");
                    button.classList.add("disabled");
                    button.setAttribute("disabled", "disabled");
                    button.textContent = "Заявка подана";
                } else {
                    console.error("Ошибка отправки формы");
                }
            }).catch(error => {
                console.error("Ошибка:", error);
            });
        });
    });
});
