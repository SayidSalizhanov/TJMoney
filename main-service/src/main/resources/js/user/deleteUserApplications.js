document.addEventListener("DOMContentLoaded", function() {
    const deleteForms = document.querySelectorAll(".delete-form");

    deleteForms.forEach(form => {
        form.addEventListener("submit", function(event) {
            event.preventDefault();

            const formData = new FormData(this);
            const applicationId = formData.get("applicationId");
            const button = document.querySelector(`#delete-button-${applicationId}`);

            fetch(this.action, {
                method: this.method,
                body: new URLSearchParams(formData)
            }).then(response => {
                if (response.ok) {
                    console.log("Запрос успешен");
                    button.classList.add("disabled");
                    button.setAttribute("disabled", "disabled");
                    button.textContent = "Заявка удалена";
                } else {
                    console.error("Ошибка отправки формы");
                }
            }).catch(error => {
                console.error("Ошибка:", error);
            });
        });
    });
});
