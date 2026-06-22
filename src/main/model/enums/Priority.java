package main.model.enums;

public enum Priority {
    VERY_LOW("Muito Baixa", 1),
    LOW     ("Baixa",       2),
    MEDIUM  ("Média",       3),
    HIGH    ("Alta",        4),
    URGENT  ("Urgente",     5);

    private final String  visualText;
    private final Integer number;

    Priority(String visualText, Integer number) {
        this.visualText = visualText;
        this.number     = number;
    }

    public String  getVisualText() { return visualText; }
    public Integer getNumber()     { return number; }

    public static Priority fromCode(int code) {
        for (Priority p : values()) {
            if (p.number == code) return p;
        }
        throw new IllegalArgumentException("Código de prioridade inválido: " + code);
    }

    @Override
    public String toString() { return visualText; }
}
