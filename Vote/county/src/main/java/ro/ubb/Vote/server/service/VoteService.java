package ro.ubb.Vote.server.service;

import ro.ubb.Vote.server.domain.ScannerVote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VoteService {
    List<ScannerVote> votes = new ArrayList();
    int lastSendNr = 0;


    public void addVote(ScannerVote vote){
        votes.add(vote);
    }


    public void sendVotes(){
        if(lastSendNr==0 ){
            compute();
        }else if(checkIfCanSend()){
            compute();
        } else {
            System.out.println("data not changed");
            System.out.println(votes);
        }
    }

    void compute(){
        lastSendNr = votes.size();
        int votes_a = 0;
        int votes_b = 0;
        int votes_c = 0;
        for(int i=0;i<votes.size();++i){
            votes_a+=votes.get(i).candidate_a;
            votes_b+=votes.get(i).candidate_b;
            votes_c+=votes.get(i).candidate_c;
        }
        System.out.println("Size: "+votes.size() + " - (a: "+votes_a + ", b:" + votes_b + ", c:" + votes_c + ")");}

    boolean checkIfCanSend(){
        return votes.size()!=lastSendNr;
    }
}
