import gensim
from gensim.models import Word2Vec
from gensim.models.keyedvectors import KeyedVectors
from sklearn.metrics.pairwise import cosine_similarity
import sys
import json
import os


def findBestMatchElement(model, jsonObject, size):
    max_similarity = 0.14
    index = -2
    describedLocator = jsonObject[str(-1)]
    # print(describedLocator)
    for i in range(size):
        describedElement = jsonObject[str(i)]
        similarity = model.n_similarity(describedLocator, describedElement)
        # print(similarity)
        if similarity > max_similarity:
            max_similarity = similarity
            index = i
    return index    

if __name__ == "__main__":
    path = r'E:\LAB UI\UITestingLocatorDetector\src\main\resources\GoogleNews-vectors-negative300.bin'
    model = KeyedVectors.load_word2vec_format(path, binary=True)
    model.save("word2vec.model")
    jsonString = os.environ["JSON_DATA"]
    # print(jsonString)
    size = sys.argv[1]
    # print(size)
    jsonObject = json.loads(jsonString)
    result = findBestMatchElement(model, jsonObject, int(size))
    print(result)
  







