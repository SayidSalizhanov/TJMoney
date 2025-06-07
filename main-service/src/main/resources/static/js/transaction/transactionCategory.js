document.addEventListener('DOMContentLoaded', function () {
    const typeSelect = document.getElementById('type');
    const categorySelect = document.getElementById('category');

    const incomeCategories = [
        "Заработная плата",
        "Прибыль от бизнеса",
        "Дивиденды",
        "Аренда",
        "Премии и бонусы",
        "Интересы",
        "Пенсия и пособия"
    ];

    const expenseCategories = [
        "Еда и напитки",
        "Транспорт",
        "Жилье",
        "Развлечения",
        "Одежда",
        "Здоровье",
        "Образование"
    ];

    const updateCategories = () => {
        const selectedType = typeSelect.value;
        categorySelect.innerHTML = '';

        let categoriesToShow;
        if (selectedType === "Доход") {
            categoriesToShow = incomeCategories;
        } else {
            categoriesToShow = expenseCategories;
        }

        categoriesToShow.forEach(category => {
            const option = document.createElement('option');
            option.value = category;
            option.textContent = category;
            categorySelect.appendChild(option);
        });

        const otherOption = document.createElement('option');
        otherOption.value = "Другое";
        otherOption.textContent = "Другое";
        categorySelect.appendChild(otherOption);
    };

    typeSelect.addEventListener('change', updateCategories);

    updateCategories();
});
