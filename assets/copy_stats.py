import firebase_admin
from firebase_admin import credentials, firestore

# Inicializar Firebase con credenciales
cred = credentials.Certificate("/home/sgaleraa/daily/firebase/gaztelubira-2067b-firebase-adminsdk-6qqkb-9176652ddf.json")
firebase_admin.initialize_app(cred)

# Conectar a Firestore
db = firestore.client()

# Definir la ruta de la subcolección de origen y la colección destino
origen = "players/2024/stats"  # Ruta completa de la subcolección de origen
destino = "players/2024/copy"  # Ruta de la subcolección destino

def copy_collection_to_other_place(origen, destino):
    # Recorrer los documentos de la colección de origen
    docs = db.collection("players").document("2024").collection("stats").stream()
    for doc in docs:
        data = doc.to_dict()  # Obtener los datos del documento
        # Copiar el documento a la subcolección de destino
        db.collection("players").document("2024").collection("copy").document(doc.id).set(data)

    print(f"Se han copiado todos los documentos de la subcolección '{origen}' a la subcolección '{destino}'.")

# Ejecutar la función de copia
copy_collection_to_other_place(origen, destino)
