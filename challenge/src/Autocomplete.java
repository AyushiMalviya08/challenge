import java.util.*;

class Node {
    Map<Character, Node> children;
    int weight;

    Node() {
        children = new HashMap<>();
        weight = 0;
    }
}

class Trie {
    Node root;

    Trie() {
        root = new Node();
    }

    void insert(String word, int weight) {
        Node node = root;
        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new Node());
            node = node.children.get(c);
        }
        node.weight += weight;
    }

    List<Map.Entry<String, Integer>> getTopK(String prefix, int k) {
        Node node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return Collections.emptyList();
            }
            node = node.children.get(c);
        }
        List<Map.Entry<String, Integer>> words = new ArrayList<>();
        dfs(node, new StringBuilder(prefix), words);
        words.sort(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()));
        return words.subList(0, Math.min(k, words.size()));
    }

    void dfs(Node node, StringBuilder prefix, List<Map.Entry<String, Integer>> words) {
        if (node.weight > 0) {
            words.add(new AbstractMap.SimpleEntry<>(prefix.toString(), node.weight));
        }
        for (char c : node.children.keySet()) {
            dfs(node.children.get(c), prefix.append(c), words);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }
}

public class Autocomplete {
    public static void main(String[] args) {
        String[] words = {"apple", "apples", "banana", "bandana", "cat", "cow"};
        int[] weights = {10, 5, 15, 7, 20, 8};

        Trie trie = new Trie();
        for (int i = 0; i < words.length; i++) {
            trie.insert(words[i], weights[i]);
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter prefix: ");
            String prefix = scanner.nextLine();
            System.out.print("Enter k: ");
            int k = Integer.parseInt(scanner.nextLine());
            List<Map.Entry<String, Integer>> results = trie.getTopK(prefix, k);
            if (!results.isEmpty()) {
                System.out.println("Autocomplete suggestions:");
                for (int i = 0; i < results.size(); i++) {
                    Map.Entry<String, Integer> entry = results.get(i);
                    System.out.printf("%d. %s (%d)\n", i + 1, entry.getKey(), entry.getValue());
                }
            } else {
                System.out.println("No autocomplete suggestions.");
            }
        }
    }
}
