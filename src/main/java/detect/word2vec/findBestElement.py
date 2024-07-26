import gensim
from gensim.models import Word2Vec
from gensim.models.keyedvectors import KeyedVectors
from sklearn.metrics.pairwise import cosine_similarity
import sys


def findBestMatchElement(model, x, y):
    max_similarity = 0
    for i in y:
        similarity = model.n_similarity(x, y)
        if (similarity > max_similarity)
            max_similarity = similarity
    
    

    
    
# print(model.n_similarity(['sushi', 'shop'], ['japanese', 'restaurant']))
# print(model.n_similarity(['save', 'and', 'end'], ['save', 'close', 'joomla', 'submitbutton','user','save']))
# print(model.n_similarity(['login'], ['sign in']))
# print(model.n_similarity(['login'], ['sign' ,'in']))

if __name__ == "__main__":
    path = r'E:\LAB UI\UITestingLocatorDetector\src\main\resources\GoogleNews-vectors-negative300.bin'
    model = KeyedVectors.load_word2vec_format(path, binary=True)
    x = sys.argv[1]
    y = sys.argv[2]
    result = findBestMatchElement(model, x, y)
    print(result)
  







