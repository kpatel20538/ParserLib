package io.kpatel.parsers;

public interface SequenceBuilder<Bld, Prt, Out> {
    Bld getNewBuilder();

    Bld appendPart(Bld builder, Prt part);

    Out toOutput(Bld builder);

    default Bld getNewBuilder(Prt part) {
        Bld builder = getNewBuilder();
        return appendPart(builder, part);
    }
}
