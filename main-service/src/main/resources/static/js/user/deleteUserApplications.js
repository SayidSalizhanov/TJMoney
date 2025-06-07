document.addEventListener("DOMContentLoaded", function() {
    const deleteForms = document.querySelectorAll(".delete-form");

    deleteForms.forEach(form => {
        form.addEventListener("submit", function(event) {
            event.preventDefault();

            const formData = new FormData(this);
            const applicationId = formData.get("applicationId");
            const button = document.querySelector(`#delete-button-${applicationId}`);
            const applicationDiv = button.closest('.application');

            fetch(this.action, {
                method: 'DELETE',
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: new URLSearchParams({
                    applicationId: applicationId
                })
            }).then(response => {
                if (response.ok) {
                    button.classList.add("disabled");
                    button.setAttribute("disabled", "disabled");
                    button.innerHTML = '<i class="bi bi-check"></i>';
                    applicationDiv.style.opacity = "0.5";
                } else {
                    console.error("Ошибка удаления заявки");
                }
            }).catch(error => {
                console.error("Ошибка:", error);
            });
        });
    });
});
