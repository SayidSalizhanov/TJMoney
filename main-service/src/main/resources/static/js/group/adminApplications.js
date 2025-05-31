document.addEventListener("DOMContentLoaded", function() {
    const actionForms = document.querySelectorAll(".action-form");

    actionForms.forEach(form => {
        form.addEventListener("submit", function(event) {
            event.preventDefault();

            const formData = new FormData(this);
            const applicationId = formData.get("applicationId");
            const approveButton = document.querySelector(`#approve-button-${applicationId}`);
            const rejectButton = document.querySelector(`#reject-button-${applicationId}`);

            fetch(this.action, {
                method: this.method,
                body: new URLSearchParams(formData)
            }).then(response => {
                if (response.ok) {
                    console.log("Запрос успешен");
                    approveButton.classList.add("disabled");
                    approveButton.setAttribute("disabled", "disabled");
                    rejectButton.classList.add("disabled");
                    rejectButton.setAttribute("disabled", "disabled");

                    if (formData.get("applicationStatus") === "Одобрено") {
                        approveButton.textContent = "Принято";
                    } else {
                        rejectButton.textContent = "Отклонено";
                    }
                } else {
                    console.error("Ошибка отправки формы");
                }
            }).catch(error => {
                console.error("Ошибка:", error);
            });
        });
    });
});
