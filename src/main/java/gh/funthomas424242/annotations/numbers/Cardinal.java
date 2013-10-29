package gh.funthomas424242.annotations.numbers;


//@Target(value = {ElementType.LOCAL_VARIABLE})
public @interface Cardinal {
    
    public int min() default 0;
    public int max() default 65535;

}
