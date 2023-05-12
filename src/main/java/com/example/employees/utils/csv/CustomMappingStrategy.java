//package com.example.employees.utils.csv;
//
//import com.opencsv.bean.ColumnPositionMappingStrategy;
//import com.opencsv.bean.MappingStrategy;
//import com.opencsv.exceptions.CsvChainedException;
//import com.opencsv.exceptions.CsvFieldAssignmentException;
//
//import java.util.Arrays;
//
//public class CustomMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {
//    public MappingStrategy<T> createMappingStrategy() {
//        return new ColumnPositionMappingStrategy<T>() {
//            @Override
//            public T populateNewBean(String[] line) throws CsvFieldAssignmentException,
//                    CsvChainedException {
//                Arrays.setAll(line, (i) -> line[i].trim());
//                return super.populateNewBean(line);
//            }
//        };
//    }
//
//    /**
//     * This method returns an empty array.
//     * The column position mapping strategy assumes that there is no header, and
//     * thus it also does not write one, accordingly.
//     * @return An empty array
//     */
//    @Override
//    public String[] generateHeader() {
//        return new String[0];
//    }
//}
