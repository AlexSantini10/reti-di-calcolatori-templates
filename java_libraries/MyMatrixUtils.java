package utils;

import java.util.ArrayList;

/**
 * Classe di utilità per la gestione di matrici
 * 
 * @author Alex Santini
 * @version 1.0
 */
public class MyMatrixUtils {

    // Base

    /**
     * Verifica se una matrice è vuota
     * 
     * @param matrix
     * @return
     */
    public boolean isEmpty(ArrayList<ArrayList<Number>> matrix) {
        return matrix.size() == 0;
    }

    /**
     * Verifica se due matrici sono uguali
     * 
     * @param matrix
     * @param matrix2
     * @return
     */
    public boolean areEquals(ArrayList<ArrayList<Number>> matrix, ArrayList<ArrayList<Number>> matrix2) {
        if (matrix.size() != matrix2.size()) {
            return false;
        }

        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(i).size() != matrix2.get(i).size()) {
                return false;
            }

            for (int j = 0; j < matrix.get(i).size(); j++) {
                if (matrix.get(i).get(j) != matrix2.get(i).get(j)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Verifica se una matrice è quadrata
     * 
     * @param matrix
     * @return
     */
    public boolean isSquare(ArrayList<ArrayList<Number>> matrix) {
        return matrix.size() == matrix.get(0).size();
    }

    /**
     * Verifica se una matrice è simmetrica
     * 
     * @param matrix
     * @return
     */
    public boolean isSymmetric(ArrayList<ArrayList<Number>> matrix) {
        if (!isSquare(matrix)) {
            return false;
        }

        for (int i = 0; i < matrix.size(); i++) {
            for (int j = i; j < matrix.get(i).size(); j++) {
                if (matrix.get(i).get(j) != matrix.get(j).get(i)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Verifica se una matrice è triangolare superiore
     * 
     * @param matrix
     * @return
     */
    public boolean isUpperTriangular(ArrayList<ArrayList<Number>> matrix) {
        if (!isSquare(matrix)) {
            return false;
        }

        for (int i = 0; i < matrix.size(); i++) {
            for (int j = i; j < matrix.get(i).size(); j++) {
                if (matrix.get(i).get(j).intValue() != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Verifica se una matrice è triangolare inferiore
     * 
     * @param matrix
     * @return
     */
    public boolean isLowerTriangular(ArrayList<ArrayList<Number>> matrix) {
        if (!isSquare(matrix)) {
            return false;
        }

        for (int i = 0; i < matrix.size(); i++) {
            for (int j = i; j < matrix.get(i).size(); j++) {
                if (matrix.get(j).get(i).intValue() != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Verifica se una matrice è diagonale
     * 
     * @param matrix
     * @return
     */
    public boolean isDiagonal(ArrayList<ArrayList<Number>> matrix) {
        if (!isSquare(matrix)) {
            return false;
        }

        for (int i = 0; i < matrix.size(); i++) {
            for (int j = i; j < matrix.get(i).size(); j++) {
                if (i != j && matrix.get(i).get(j).intValue() != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Verifica se una matrice è identità
     * 
     * @param matrix
     * @return
     */
    public boolean isIdentity(ArrayList<ArrayList<Number>> matrix) {
        if (!isDiagonal(matrix)) {
            return false;
        }

        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(i).get(i).intValue() != 1) {
                return false;
            }
        }

        return true;
    }



    // Diagonali

    /**
     * Restituisce la diagonale principale di una matrice
     * 
     * @param matrix
     * @return
     */
    public ArrayList<Number> getMainDiagonal(ArrayList<ArrayList<Number>> matrix) {
        ArrayList<Number> mainDiagonal = new ArrayList<Number>();

        for (int i = 0; i < matrix.size(); i++) {
            mainDiagonal.add(matrix.get(i).get(i));
        }

        return mainDiagonal;
    }

    /**
     * Restituisce la diagonale secondaria di una matrice
     * 
     * @param matrix
     * @return
     */
    public ArrayList<Number> getSecondaryDiagonal(ArrayList<ArrayList<Number>> matrix) {
        ArrayList<Number> secondaryDiagonal = new ArrayList<Number>();

        for (int i = 0; i < matrix.size(); i++) {
            secondaryDiagonal.add(matrix.get(i).get(matrix.size() - i - 1));
        }

        return secondaryDiagonal;
    }

    // Operazioni

    /**
     * Restituisce la somma degli elementi di una matrice
     * 
     * @param matrix
     * @return
     */
    public Number getSum(ArrayList<ArrayList<Number>> matrix) {
        Number sum = null;

        for (int i = 0; i < matrix.size(); i++) {
            for (int j = i; j < matrix.get(i).size(); j++) {
                sum = sum.floatValue() + matrix.get(i).get(j).floatValue();
            }
        }

        return sum;
    }

    /**
     * Restituisce il prodotto degli elementi di una matrice
     * 
     * @param matrix
     * @return
     */
    public Number getProduct(ArrayList<ArrayList<Number>> matrix) {
        Number product = null;

        for (int i = 0; i < matrix.size(); i++) {
            for (int j = i; j < matrix.get(i).size(); j++) {
                product = product.floatValue() * matrix.get(i).get(j).floatValue();
            }
        }

        return product;
    }

    // Operazioni con scalari
    /**
     * Restituisce la moltplicazione di una matrice per uno scalare
     * 
     * @param matrix
     * @param scalar
     * @return
     */
    public ArrayList<ArrayList<Number>> multiplyByScalar(ArrayList<ArrayList<Number>> matrix, Number scalar) {
        ArrayList<ArrayList<Number>> result = new ArrayList<ArrayList<Number>>();

        for (int i = 0; i < matrix.size(); i++) {
            ArrayList<Number> row = new ArrayList<Number>();

            for (int j = 0; j < matrix.get(i).size(); j++) {
                row.add(matrix.get(i).get(j).floatValue() * scalar.floatValue());
            }

            result.add(row);
        }

        return result;
    }

    /**
     * Restituisce la sommma di due matrici
     * 
     * @param matrix
     * @param matrix2
     * @return
     */
    public ArrayList<ArrayList<Number>> sum(ArrayList<ArrayList<Number>> matrix, ArrayList<ArrayList<Number>> matrix2) {
        ArrayList<ArrayList<Number>> result = new ArrayList<ArrayList<Number>>();

        for (int i = 0; i < matrix.size(); i++) {
            ArrayList<Number> row = new ArrayList<Number>();

            for (int j = 0; j < matrix.get(i).size(); j++) {
                row.add(matrix.get(i).get(j).floatValue() + matrix2.get(i).get(j).floatValue());
            }

            result.add(row);
        }

        return result;
    }

    /**
     * Restituisce la differenza di due matrici
     * 
     * @param matrix
     * @param matrix2
     * @return
     */
    public ArrayList<ArrayList<Number>> difference(ArrayList<ArrayList<Number>> matrix,
            ArrayList<ArrayList<Number>> matrix2) {
        ArrayList<ArrayList<Number>> result = new ArrayList<ArrayList<Number>>();

        for (int i = 0; i < matrix.size(); i++) {
            ArrayList<Number> row = new ArrayList<Number>();

            for (int j = 0; j < matrix.get(i).size(); j++) {
                row.add(matrix.get(i).get(j).floatValue() - matrix2.get(i).get(j).floatValue());
            }

            result.add(row);
        }

        return result;
    }

    /**
     * Restituisce il prodotto matriciale
     * 
     * @param matrix
     * @param matrix2
     * @return
     */
    public ArrayList<ArrayList<Number>> multiplyMatrices(ArrayList<ArrayList<Number>> matrix,
            ArrayList<ArrayList<Number>> matrix2) {
        if (matrix.get(0).size() != matrix2.size()) {
            throw new IllegalArgumentException(
                    "Impossibile moltiplicare le matrici: numero di colonne della prima matrice diverso dal numero di righe della seconda matrice");
        }

        ArrayList<ArrayList<Number>> result = new ArrayList<>();

        for (int i = 0; i < matrix.size(); i++) {
            ArrayList<Number> newRow = new ArrayList<>();
            for (int j = 0; j < matrix2.get(0).size(); j++) {
                newRow.add(0);
            }
            result.add(newRow);
        }

        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix2.get(0).size(); j++) {
                for (int k = 0; k < matrix.get(0).size(); k++) {
                    result.get(i).set(j, result.get(i).get(j).floatValue()
                            + matrix.get(i).get(k).floatValue() * matrix2.get(k).get(j).floatValue());
                }
            }
        }

        return result;
    }

    /**
     * Restituisce la trasposta di una matrice
     * 
     * @param matrix
     * @return
     */
    public ArrayList<ArrayList<Number>> transpose(ArrayList<ArrayList<Number>> matrix) {
        ArrayList<ArrayList<Number>> result = new ArrayList<ArrayList<Number>>();

        for (int i = 0; i < matrix.get(0).size(); i++) {
            ArrayList<Number> row = new ArrayList<Number>();

            for (int j = 0; j < matrix.size(); j++) {
                row.add(matrix.get(j).get(i));
            }

            result.add(row);
        }

        return result;
    }

    
    // Rango
    /**
     * Restituisce il rango di una matrice
     * 
     * @param matrix
     * @return
     */
    public int getRank(ArrayList<ArrayList<Number>> matrix) {
        ArrayList<ArrayList<Number>> matrixCopy = new ArrayList<ArrayList<Number>>();

        for (int i = 0; i < matrix.size(); i++) {
            ArrayList<Number> row = new ArrayList<Number>();

            for (int j = 0; j < matrix.get(i).size(); j++) {
                row.add(matrix.get(i).get(j));
            }

            matrixCopy.add(row);
        }

        int rank = 0;

        for (int i = 0; i < matrixCopy.size(); i++) {
            if (matrixCopy.get(i).get(i).intValue() == 0) {
                for (int j = i + 1; j < matrixCopy.size(); j++) {
                    if (matrixCopy.get(j).get(i).intValue() != 0) {
                        ArrayList<Number> temp = matrixCopy.get(i);
                        matrixCopy.set(i, matrixCopy.get(j));
                        matrixCopy.set(j, temp);
                        break;
                    }
                }
            }

            if (matrixCopy.get(i).get(i).intValue() == 0) {
                continue;
            }

            for (int j = i + 1; j < matrixCopy.size(); j++) {
                Number coefficient = matrixCopy.get(j).get(i).floatValue() / matrixCopy.get(i).get(i).floatValue();

                for (int k = i; k < matrixCopy.get(i).size(); k++) {
                    matrixCopy.get(j).set(k, matrixCopy.get(j).get(k).floatValue()
                            - matrixCopy.get(i).get(k).floatValue() * coefficient.floatValue());
                }
            }

            rank++;
        }

        return rank;
    }

    // Determinante
    /**
     * Restituisce il determinante di una matrice
     * 
     * @param matrix
     * @return
     */
    public Number getDeterminant(ArrayList<ArrayList<Number>> matrix) {
        if (!isSquare(matrix)) {
            throw new IllegalArgumentException("Impossibile calcolare il determinante di una matrice non quadrata");
        }

        if (matrix.size() == 1) {
            return matrix.get(0).get(0);
        }

        if (matrix.size() == 2) {
            return matrix.get(0).get(0).floatValue() * matrix.get(1).get(1).floatValue()
                    - matrix.get(0).get(1).floatValue() * matrix.get(1).get(0).floatValue();
        }

        Number determinant = 0;

        for (int i = 0; i < matrix.size(); i++) {
            ArrayList<ArrayList<Number>> subMatrix = new ArrayList<ArrayList<Number>>();

            for (int j = 1; j < matrix.size(); j++) {
                ArrayList<Number> row = new ArrayList<Number>();

                for (int k = 0; k < matrix.get(j).size(); k++) {
                    if (k != i) {
                        row.add(matrix.get(j).get(k));
                    }
                }

                subMatrix.add(row);
            }

            determinant = determinant.floatValue() + matrix.get(0).get(i).floatValue()
                    * getDeterminant(subMatrix).floatValue() * Math.pow(-1, i);
        }

        return determinant;
    }
}
