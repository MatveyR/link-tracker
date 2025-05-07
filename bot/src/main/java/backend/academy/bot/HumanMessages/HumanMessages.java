package backend.academy.bot.HumanMessages;

public enum HumanMessages {
    HELLO("Привет! Это бот для отслеживания обновлений на форумах. Введите \"/help\" для помощи"),
    ERROR("Я не умею на такое реагировать :(" + System.lineSeparator() + "Попробуйте ещё раз"),
    HELP("/start - Запуск бота" + System.lineSeparator()
            + "/help - Доступные команды" + System.lineSeparator()
            + "/track <ссылка> - Начать отслеживать ресурс. Укажите URL" + System.lineSeparator()
            + "/untrack <ссылка> - Перестать отслеживать ресурс. Укажите URL" + System.lineSeparator()
            + "/list - Показать список отслеживаемых ресурсов"),
    TRACK_LINK_ERROR("Указана неверная ссылка :(" + System.lineSeparator()
            + "Убедитесь, что ссылка прикрепили ссылку в формате \"/track <ссылка>\" и что её формат верный"),
    ENTER_LINK_TAGS("При желании введите через пробел тэги для отслеживаемой ссылки" + System.lineSeparator()
            + "В противном случае нажмите \"Пропустить\""),
    ENTER_LINK_FILTERS("При желании введите фильтры для отслеживаемой ссылки" + System.lineSeparator()
            + "В противном случае нажмите \"Пропустить\""),
    TRACK_LINK_COMPLETED("Отлично! Отслеживание на ссылку установлено"),
    LINKS_HEADER("Ваши отслеживаемые ссылки:"),
    UNTRACK_ERROR("Вы не подписаны на такую ссылку"),
    UNTRACK_COMPLETED("Ссылка удалена из отслеживания"),
    LIST_ERROR("Ошибка при получении списка ссылок"),
    NO_LINKS("Нет отслеживаемых ссылок");

    private final String text;

    HumanMessages(String message) {
        this.text = message;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
