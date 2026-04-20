import java.util.*;

public class Apriori {

    static List<Set<String>> transactions = new ArrayList<>();
    static double minSupport = 0.5;   // 50%
    static double minConfidence = 0.7; // 70%

    public static void main(String[] args) {

        loadData();

        Map<Set<String>, Integer> frequentItemsets = generateFrequentItemsets();

        System.out.println("Frequent Itemsets:");
        for (Set<String> itemset : frequentItemsets.keySet()) {
            System.out.println(itemset + " -> " + frequentItemsets.get(itemset));
        }

        System.out.println("\nAssociation Rules:");
        generateAssociationRules(frequentItemsets);
    }

    static void loadData() {
        transactions.add(new HashSet<>(Arrays.asList("Milk","Bread","Butter")));
        transactions.add(new HashSet<>(Arrays.asList("Bread","Butter")));
        transactions.add(new HashSet<>(Arrays.asList("Milk","Bread")));
        transactions.add(new HashSet<>(Arrays.asList("Milk","Butter")));
        transactions.add(new HashSet<>(Arrays.asList("Bread","Butter")));
        transactions.add(new HashSet<>(Arrays.asList("Milk","Bread","Butter")));
    }

    static Map<Set<String>, Integer> generateFrequentItemsets() {
        Map<Set<String>, Integer> freqMap = new HashMap<>();

        Map<String, Integer> itemCount = new HashMap<>();

        for (Set<String> transaction : transactions) {
            for (String item : transaction) {
                itemCount.put(item, itemCount.getOrDefault(item, 0) + 1);
            }
        }

        int totalTransactions = transactions.size();

        List<Set<String>> currentItemsets = new ArrayList<>();

        for (String item : itemCount.keySet()) {
            double support = (double)itemCount.get(item) / totalTransactions;
            if (support >= minSupport) {
                Set<String> set = new HashSet<>();
                set.add(item);
                currentItemsets.add(set);
                freqMap.put(set, itemCount.get(item));
            }
        }

        while (!currentItemsets.isEmpty()) {
            List<Set<String>> newItemsets = new ArrayList<>();

            for (int i = 0; i < currentItemsets.size(); i++) {
                for (int j = i + 1; j < currentItemsets.size(); j++) {
                    Set<String> union = new HashSet<>(currentItemsets.get(i));
                    union.addAll(currentItemsets.get(j));

                    if (union.size() == currentItemsets.get(i).size() + 1) {
                        int count = countSupport(union);

                        double support = (double)count / totalTransactions;

                        if (support >= minSupport && !freqMap.containsKey(union)) {
                            newItemsets.add(union);
                            freqMap.put(union, count);
                        }
                    }
                }
            }

            currentItemsets = newItemsets;
        }

        return freqMap;
    }

    static int countSupport(Set<String> itemset) {
        int count = 0;
        for (Set<String> transaction : transactions) {
            if (transaction.containsAll(itemset)) {
                count++;
            }
        }
        return count;
    }

    static void generateAssociationRules(Map<Set<String>, Integer> freqMap) {
        int totalTransactions = transactions.size();

        for (Set<String> itemset : freqMap.keySet()) {
            if (itemset.size() < 2) continue;

            for (String item : itemset) {
                Set<String> left = new HashSet<>(itemset);
                left.remove(item);

                Set<String> right = new HashSet<>();
                right.add(item);

                int leftSupport = countSupport(left);
                int fullSupport = freqMap.get(itemset);

                double confidence = (double) fullSupport / leftSupport;

                if (confidence >= minConfidence) {
                    System.out.println(left + " -> " + right + 
                        " (Confidence: " + confidence + ")");
                }
            }
        }
    }
}