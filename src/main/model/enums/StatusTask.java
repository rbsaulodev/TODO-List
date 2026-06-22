package main.model.enums;

public enum StatusTask {
    TODO ("A Fazer",    1),
    DOING("Em Andamento", 2),
    DONE ("Concluída",  3);

    private final String  visualText;
    private final Integer number;

    StatusTask(String visualText, Integer number) {
        this.visualText = visualText;
        this.number     = number;
    }

    public String  getVisualText() { return visualText; }
    public Integer getNumber()     { return number; }

    public static StatusTask fromCode(int code) {
        for (StatusTask s : values()) {
            if (s.number == code) return s;
        }
        throw new IllegalArgumentException("Código de status inválido: " + code);
    }

    @Override
    public String toString() { return visualText; }
}
