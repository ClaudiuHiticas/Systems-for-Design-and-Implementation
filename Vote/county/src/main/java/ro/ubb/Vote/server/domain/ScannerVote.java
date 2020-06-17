package ro.ubb.Vote.server.domain;

public class ScannerVote {
    String scannerName;
    public int candidate_a;
    public int candidate_b;
    public int candidate_c;

    public ScannerVote(String scannerName, int candidate_a, int candidate_b, int candidate_c) {
        this.scannerName = scannerName;
        this.candidate_a = candidate_a;
        this.candidate_b = candidate_b;
        this.candidate_c = candidate_c;
    }
}
