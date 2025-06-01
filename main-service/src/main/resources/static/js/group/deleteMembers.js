document.addEventListener("DOMContentLoaded", function() {
    const deleteForms = document.querySelectorAll(".delete-form");

    deleteForms.forEach(form => {
        form.addEventListener("submit", function(event) {
            event.preventDefault();

            const formData = new FormData(this);
            const userIdForDelete = formData.get("userIdForDelete")
            const button = document.querySelector(`#delete-button-${userIdForDelete}`);

            fetch(this.action, {
                method: 'DELETE',
                body: new URLSearchParams(formData)
            }).then(response => {
                if (response.ok) {
                    console.log("Запрос успешен");
                    button.classList.add("disabled");
                    button.setAttribute("disabled", "disabled");
                    button.textContent = "Удалено";
                } else {
                    console.error("Ошибка отправки формы");
                }
            }).catch(error => {
                console.error("Ошибка:", error);
            });
        });
    });
});
