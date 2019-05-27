package org.sistemafiesc.lucasftecnico.exemplofirebasefirestoremultiplo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText edtTitulo, edtDescricao, edtPrioridade, edtTags;
    private TextView tvNota;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference notasCollectionReference = firestore.collection("Notas");
    private DocumentSnapshot ultimoResultado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTitulo = findViewById(R.id.edtTitulo);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtPrioridade = findViewById(R.id.edtPrioridade);
        edtTags = findViewById(R.id.edtTags);
        tvNota = findViewById(R.id.tvNota);
    }


    public void adicionarNota(View view) {
        // Na parte 17 do tutorial do Firestore, aprenderemos a armazenar, atualizar e consultar objetos aninhados. Podemos acessar campos de objetos ou mapas dentro de um documento com "notação de ponto", onde encadeamos as chaves de campos hierárquicos juntos para alcançar o valor aninhado. Podemos modificar e consultar esses valores da mesma forma que os campos de nível superior com atualização, FieldValue.delete e assim por diante.
        String titulo = edtTitulo.getText().toString();
        String descricao = edtDescricao.getText().toString();

        if (edtPrioridade.length() == 0) {
            edtPrioridade.setText("0");
        }

        int prioridade = Integer.parseInt(edtPrioridade.getText().toString());
        String tagInput = edtTags.getText().toString();
        String[] tagArray = tagInput.split("\\s*,\\s*");
        Map<String, Boolean> tags = new HashMap<>();

        for (String tag : tagArray) {
            tags.put(tag, true);
        }

        Nota nota = new Nota(titulo, descricao, prioridade, tags);
        notasCollectionReference.document("0GGWj4VOBhgn6nELgrQr").collection("Child Notas").add(nota);
    }

    ///

    public void carregarNotas(View view) {
        // Na parte 17 do tutorial do Firestore, aprenderemos a armazenar, atualizar e consultar objetos aninhados. Podemos acessar campos de objetos ou mapas dentro de um documento com "notação de ponto", onde encadeamos as chaves de campos hierárquicos juntos para alcançar o valor aninhado. Podemos modificar e consultar esses valores da mesma forma que os campos de nível superior com atualização, FieldValue.delete e assim por diante.
        notasCollectionReference.document("0GGWj4VOBhgn6nELgrQr").collection("Child Notas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String dados = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Nota nota = documentSnapshot.toObject(Nota.class);
                    nota.setDocumentoId(documentSnapshot.getId());

                    String documentoId = nota.getDocumentoId();
                    dados += "ID: " + documentoId;

                    for (String tag : nota.getTags().keySet()) {
                        dados += "\n-" + tag;
                    }
                    dados += "\n\n";
                }
                tvNota.setText(dados);
            }
        });
    }

}
// https://www.youtube.com/watch?v=errtXHEvzsc&list=PLrnPJCHvNZuBf5KH4XXOthtgo6E4Epjl8&index=26


/*
   private void atualizaValorAninhado() {
        // Na parte 17 do tutorial do Firestore, aprenderemos a armazenar, atualizar e consultar objetos aninhados. Podemos acessar campos de objetos ou mapas dentro de um documento com "notação de ponto", onde encadeamos as chaves de campos hierárquicos juntos para alcançar o valor aninhado. Podemos modificar e consultar esses valores da mesma forma que os campos de nível superior com atualização, FieldValue.delete e assim por diante.
        notasCollectionReference.document("0GGWj4VOBhgn6nELgrQr").//update("tags", FieldValue.arrayUnion("nova tag"));
                update("tags.tag1.nested1.nested2", true);
    }

    public void adicionarNota(View view) {
        // Na parte 17 do tutorial do Firestore, aprenderemos a armazenar, atualizar e consultar objetos aninhados. Podemos acessar campos de objetos ou mapas dentro de um documento com "notação de ponto", onde encadeamos as chaves de campos hierárquicos juntos para alcançar o valor aninhado. Podemos modificar e consultar esses valores da mesma forma que os campos de nível superior com atualização, FieldValue.delete e assim por diante.
        String titulo = edtTitulo.getText().toString();
        String descricao = edtDescricao.getText().toString();

        if (edtPrioridade.length() == 0) {
            edtPrioridade.setText("0");
        }

        int prioridade = Integer.parseInt(edtPrioridade.getText().toString());
        String tagInput = edtTags.getText().toString();
        String[] tagArray = tagInput.split("\\s*,\\s*");
        Map<String, Boolean> tags = new HashMap<>();

        for (String tag : tagArray) {
            tags.put(tag, true);
        }

        Nota nota = new Nota(titulo, descricao, prioridade, tags);
        notasCollectionReference.document("0GGWj4VOBhgn6nELgrQr").collection("Child Notas").add(nota);

        notasCollectionReference.add(nota);
    }

    ///

    public void carregarNotas(View view) {
        // Na parte 17 do tutorial do Firestore, aprenderemos a armazenar, atualizar e consultar objetos aninhados. Podemos acessar campos de objetos ou mapas dentro de um documento com "notação de ponto", onde encadeamos as chaves de campos hierárquicos juntos para alcançar o valor aninhado. Podemos modificar e consultar esses valores da mesma forma que os campos de nível superior com atualização, FieldValue.delete e assim por diante.
        notasCollectionReference.whereEqualTo("tags.tag1", true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String dados = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Nota nota = documentSnapshot.toObject(Nota.class);
                    nota.setDocumentoId(documentSnapshot.getId());

                    String documentoId = nota.getDocumentoId();
                    dados += "ID: " + documentoId;

                    for (String tag : nota.getTags().keySet()) {
                        dados += "\n-" + tag;
                    }
                    dados += "\n\n";
                }
                tvNota.setText(dados);
            }
        });
    }
 */

////////////////////

/*
public void adicionarNota(View view) {
        String titulo = edtTitulo.getText().toString();
        String descricao = edtDescricao.getText().toString();

        if (edtPrioridade.length() == 0) {
            edtPrioridade.setText("0");
        }

        int prioridade = Integer.parseInt(edtPrioridade.getText().toString());
        String tagInput = edtTags.getText().toString();
        String[] tagArray = tagInput.split("\\s*,\\s*");
        List<String> tags = Arrays.asList(tagArray);

        Nota nota = new Nota(titulo, descricao, prioridade, tags);

        notasCollectionReference.add(nota);
    }


     public void carregarNotas(View view) {
        notasCollectionReference.whereArrayContains("tags", "kg").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String dados = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Nota nota = documentSnapshot.toObject(Nota.class);
                    nota.setDocumentoId(documentSnapshot.getId());

                    String documentoId = nota.getDocumentoId();
                    dados += "ID: " + documentoId;

                    for (String tag : nota.getTags().keySet()) {
                        dados += "\n-" + tag;
                    }
                    dados += "\n\n";
                }
                tvNota.setText(dados);
            }
        });
    }

     private void atualizaArray() {
        notasCollectionReference.document("e4x8Oo1GJhcOnjzRulAK").//update("tags", FieldValue.arrayUnion("nova tag"));
                update("tags", FieldValue.arrayRemove("nova tag"));
    }
 */

/*
 public void carregarNotas(View view) {
        Query query;
        if (ultimoResultado == null) {
            query = notasCollectionReference.orderBy("prioridade").limit(3);
        } else {
            query = notasCollectionReference.orderBy("prioridade").startAfter(ultimoResultado).limit(3);
        }

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String dados = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Nota nota = documentSnapshot.toObject(Nota.class);
                    nota.setDocumentoId(documentSnapshot.getId());
                    dados += "ID: " + nota.getDocumentoId() + "\n" + "Título: " + nota.getTitulo() + "\n" + "Descrição: " + nota.getDescricao() + "\n" + "Prioridade: " + nota.getPrioridade() + "\n\n";
                }
                if (queryDocumentSnapshots.size() > 0) {
                    dados += "_______________________\n\n";
                    tvNota.append(dados);
                    ultimoResultado = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                }
            }
        });
    }
 */


/*
 private void executaTransacao() {
       firestore.runTransaction(new Transaction.Function<Long>() {
           @Override
           public Long apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
               DocumentReference exemploNotaReference = notasCollectionReference.document("jQoeDUutPgx5AuwglPFk");
               DocumentSnapshot exemploNotaSnapshot = transaction.get(exemploNotaReference);
               long novaPrioridade = exemploNotaSnapshot.getLong("prioridade") + 1;

               transaction.update(exemploNotaReference, "prioridade", novaPrioridade);

               return novaPrioridade;
           }
       }).addOnSuccessListener(new OnSuccessListener<Long>() {
           @Override
           public void onSuccess(Long aLong) {
               Toast.makeText(getApplicationContext(), "Nova prioridade: " + aLong, Toast.LENGTH_SHORT).show();
           }
       });
    }
 */




/*
private void executaEscritaEmLote() {
        WriteBatch batch = firestore.batch();
        DocumentReference documento01 = notasCollectionReference.document("Nova nota");
        batch.set(documento01, new Nota("Nova nota", "Nova nota", 1));

        DocumentReference documento02 = notasCollectionReference.document("7Qnexz6aSEzvyDTSNJrD");
        batch.update(documento02, "titulo", "Nota atualizada");

        DocumentReference documento03 = notasCollectionReference.document("1ZXWg912vKMG3bEFOYJ1");
        batch.delete(documento03);

        DocumentReference documento04 = notasCollectionReference.document();
        batch.set(documento04, new Nota("Nota adicionada", "Nota adicionada", 1));

        batch.commit().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tvNota.setText(e.toString());
            }
        });
    }
 */

/*
 @Override
    protected void onStart() {
        super.onStart();


        Na parte 13 do tutorial do Firestore, aprenderemos a usar o DocumentChanges para ouvir mudanças granulares em nosso conjunto de dados. Dessa forma, podemos recuperar apenas documentos específicos que foram adicionados, modificados ou removidos, em vez de sempre recuperar todos os documentos de uma consulta de uma só vez. Isso é particularmente útil se quisermos exibir nossos dados em um RecyclerView. Obtemos uma List of DocumentChange chamando getDocumentChanges no QuerySnapshot que passamos no método onEvent de um SnapshotListener. O DocumentChanges pode ter 3 tipos: ADICIONADO, MODIFICADO e REMOVIDO, dependendo do tipo de alteração ocorrida no documento. A partir desses objetos DocumentChange, podemos recuperar o DocumentSnapshot afetado chamando getDocument nele. Com getOldIndex e getNewIndex, podemos obter a posição do documento antes e depois da alteração.

        notasCollectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
@Override
public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
        return;
        }
        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
        DocumentSnapshot documentSnapshot = documentChange.getDocument();
        String id = documentSnapshot.getId();
        int oldIndex = documentChange.getOldIndex();
        int newIndex = documentChange.getNewIndex();

        switch (documentChange.getType()) {
        case ADDED:
        tvNota.append("\nAdicionado: " + id + "\nÍndice velho: " + oldIndex + "\nÍndice novo: " + newIndex);
        break;
        case MODIFIED:
        tvNota.append("\nModificado: " + id + "\nÍndice velho: " + oldIndex + "\nÍndice novo: " + newIndex);
        break;
        case REMOVED:
        tvNota.append("\nRemovido: " + id + "\nÍndice velho: " + oldIndex + "\nÍndice novo: " + newIndex);
        break;
        }
        }
        }
        });
        }
 */

/*
 public void carregarNotas(View view) {
        notasCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String dados = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Nota nota = documentSnapshot.toObject(Nota.class);
                    nota.setDocumentoId(documentSnapshot.getId());

                    String documentoId = nota.getDocumentoId();
                    String titulo = nota.getTitulo();
                    String descricao = nota.getDescricao();
                    int prioridade = nota.getPrioridade();

                    dados += "ID: " + documentoId + "\n" + "Título: " + titulo + "\n" + "Descrição: " + descricao + "\n" + "Prioridade: " + prioridade + "\n\n";
                }
                tvNota.setText(dados);
            }
        });
    }
 */


/*
public void carregarNotas(View view) {
        notasCollectionReference.whereGreaterThanOrEqualTo("prioridade", 2)
                .whereEqualTo("titulo", "aa")
                .orderBy("prioridade", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String dados = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Nota nota = documentSnapshot.toObject(Nota.class);
                    nota.setDocumentoId(documentSnapshot.getId());

                    String documentoId = nota.getDocumentoId();
                    String titulo = nota.getTitulo();
                    String descricao = nota.getDescricao();
                    int prioridade = nota.getPrioridade();

                    dados += "ID: " + documentoId + "\n" + "Título: " + titulo + "\n" + "Descrição: " + descricao + "\n" + "Prioridade: " + prioridade + "\n\n";
                }
                tvNota.setText(dados);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
            }
        });
    }

 */

/*
 public void carregarNotas(View view) {
        Task task01 = notasCollectionReference.whereLessThan("prioridade", 2)
                .orderBy("prioridade")
                .get();

        Task task02 = notasCollectionReference.whereGreaterThan("prioridade", 2)
                .orderBy("prioridade")
                .limit(3)
                .get();

        Task<List<QuerySnapshot>> todosTasks = Tasks.whenAllSuccess(task01, task02);
        todosTasks.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                String dados = "";

                for (QuerySnapshot queryDocumentSnapshots : querySnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Nota nota = documentSnapshot.toObject(Nota.class);
                        nota.setDocumentoId(documentSnapshot.getId());

                        String documentoId = nota.getDocumentoId();
                        String titulo = nota.getTitulo();
                        String descricao = nota.getDescricao();
                        int prioridade = nota.getPrioridade();

                        dados += "ID: " + documentoId + "\n" + "Título: " + titulo + "\n" + "Descrição: " + descricao + "\n" + "Prioridade: " + prioridade + "\n\n";
                    }
                }
                tvNota.setText(dados);
            }
        });
    }
 */

/*
   public void carregarNotas(View view) {
        notasCollectionReference.document("7Qnexz6aSEzvyDTSNJrD")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        notasCollectionReference.orderBy("prioridade")
                                .startAt(documentSnapshot)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        String dados = "";

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            Nota nota = documentSnapshot.toObject(Nota.class);
                                            nota.setDocumentoId(documentSnapshot.getId());
                                            dados += "ID: " + nota.getDocumentoId() + "\n" + "Título: " + nota.getTitulo() + "\n" + "Descrição: " + nota.getDescricao() + "\n" + "Prioridade: " + nota.getPrioridade() + "\n\n";
                                        }
                                        tvNota.setText(dados);
                                    }
                                });

                    }
                });
    }
 */

/*
 @Override
    protected void onStart() {
        super.onStart();
        notasCollectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                String dados = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Nota nota = documentSnapshot.toObject(Nota.class);
                    nota.setDocumentoId(documentSnapshot.getId());

                    dados += "ID: " + nota.getDocumentoId() + "\n" + "Título: " + nota.getTitulo() + "\n" + "Descrição: " + nota.getDescricao() + "\n" + "Prioridade: " + nota.getPrioridade() + "\n\n";
                }
                tvNota.setText(dados);
            }
        });
    }
 */