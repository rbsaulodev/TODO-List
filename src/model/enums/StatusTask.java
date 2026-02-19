package model.enums;

public enum StatusTask {
    TODO("[ ] Pendente", 1),
    DOING("[-] Em andamento", 2),
    DONE("[X] Finalizada", 3);

    private final String visualText;
    private final Integer number;

    StatusTask(String visualText, Integer number) {
        this.visualText = visualText;
        this.number = number;
    }

    public String getVisualText() {
        return visualText;
    }

    public Integer getNumber() {
        return number;
    }

    public static StatusTask fromCode(int code) {
        for (StatusTask status : StatusTask.values()) {
            if (status.getNumber() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status inválido: " + code);
    }

    @Override
    public String toString() {
        return visualText;
    }
}