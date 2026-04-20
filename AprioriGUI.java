import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout.*;
import java.util.*;

public class AprioriGUI extends JFrame {

    static List<Set<String>> transactions = new ArrayList<>();
    static double minSupport = 0.2;
    static double minConfidence = 0.5;

    DefaultTableModel tableModel;

    public AprioriGUI() {
        setTitle("Apriori Market Basket Analysis");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new java.awt.BorderLayout());

        JButton runButton = new JButton("Run Apriori");
        add(runButton, java.awt.BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Type");
        tableModel.addColumn("Itemset / Rule");
        tableModel.addColumn("Value");

        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), java.awt.BorderLayout.CENTER);

        runButton.addActionListener(e -> runApriori());

        setVisible(true);
    }

    void runApriori() {
        tableModel.setRowCount(0);
        transactions.clear();
        loadData();

        Map<Set<String>, Integer> freqMap = generateFrequentItemsets();

        int total = transactions.size();

        // Show frequent itemsets
        for (Set<String> itemset : freqMap.keySet()) {
            double support = (double) freqMap.get(itemset) / total;
            tableModel.addRow(new Object[]{
                    "Itemset",
                    itemset.toString(),
                    String.format("Support: %.2f", support)
            });
        }

        // Show rules
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
                    tableModel.addRow(new Object[]{
                            "Rule",
                            left + " → " + right,
                            String.format("Conf: %.2f", confidence)
                    });
                }
            }
        }
    }

    static void loadData() {
        transactions.add(new HashSet<>(Arrays.asList("Milk","Bread","Butter")));
        transactions.add(new HashSet<>(Arrays.asList("Bread","Butter")));
        transactions.add(new HashSet<>(Arrays.asList("Milk","Bread")));
        transactions.add(new HashSet<>(Arrays.asList("Milk","Butter")));
        transactions.add(new HashSet<>(Arrays.asList("Bread","Butter")));
        transactions.add(new HashSet<>(Arrays.asList("Milk","Bread","Butter")));

        transactions.add(new HashSet<>(Arrays.asList("Rice","Dal","Oil")));
        transactions.add(new HashSet<>(Arrays.asList("Rice","Oil")));
        transactions.add(new HashSet<>(Arrays.asList("Dal","Oil")));
        transactions.add(new HashSet<>(Arrays.asList("Rice","Dal")));

        transactions.add(new HashSet<>(Arrays.asList("Sugar","Tea","Biscuit")));
        transactions.add(new HashSet<>(Arrays.asList("Tea","Biscuit")));
        transactions.add(new HashSet<>(Arrays.asList("Milk","Biscuit")));

        transactions.add(new HashSet<>(Arrays.asList("Bread","Jam")));
        transactions.add(new HashSet<>(Arrays.asList("Milk","Bread","Jam")));
        transactions.add(new HashSet<>(Arrays.asList("Butter","Jam")));

        transactions.add(new HashSet<>(Arrays.asList("Rice","Salt")));
        transactions.add(new HashSet<>(Arrays.asList("Dal","Salt")));
        transactions.add(new HashSet<>(Arrays.asList("Oil","Salt")));
        transactions.add(new HashSet<>(Arrays.asList("Rice","Dal","Oil","Salt")));
    }

    static Map<Set<String>, Integer> generateFrequentItemsets() {
        Map<Set<String>, Integer> freqMap = new HashMap<>();
        Map<String, Integer> itemCount = new HashMap<>();

        for (Set<String> transaction : transactions) {
            for (String item : transaction) {
                itemCount.put(item, itemCount.getOrDefault(item, 0) + 1);
            }
        }

        int total = transactions.size();
        List<Set<String>> currentItemsets = new ArrayList<>();

        for (String item : itemCount.keySet()) {
            double support = (double)itemCount.get(item) / total;
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
                        double support = (double)count / total;

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

    public static void main(String[] args) {
        new AprioriGUI();
    }
}