import gensim
from gensim.models import Word2Vec
from gensim.models.keyedvectors import KeyedVectors
from sklearn.metrics.pairwise import cosine_similarity
import sys
import json5


def findBestMatchElement(model, jsonObject, size):
    max_similarity = 0
    index = -2
    describedLocator = jsonObject[str(-1)]
    print(describedLocator)
    for i in range(size):
        describedElement = jsonObject(str(i))
        similarity = model.n_similarity(describedLocator, describedElement)
        print(similarity)
        if similarity > max_similarity:
            max_similarity = similarity
            index = i
    return index    
# print(model.n_similarity(['sushi', 'shop'], ['japanese', 'restaurant']))
# print(model.n_similarity(['save', 'and', 'end'], ['save', 'close', 'joomla', 'submitbutton','user','save']))
# print(model.n_similarity(['login'], ['sign in']))
# print(model.n_similarity(['login'], ['sign' ,'in']))

if __name__ == "__main__":
    path = r'E:\LAB UI\UITestingLocatorDetector\src\main\resources\GoogleNews-vectors-negative300.bin'
    model = KeyedVectors.load_word2vec_format(path, binary=True)
    model.save("word2vec.model")
    jsonString = sys.argv[1]
    size = sys.argv[2]
    print(jsonString)
    print(size)
    jsonObject = json5.loads(jsonString)
    result = findBestMatchElement(model, jsonObject, size)
    print(result)
  







