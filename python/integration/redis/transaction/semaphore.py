#!/opt/homebrew/bin/python3
# encoding=utf-8

from redis import Redis
from redis import WatchError

"""
信号量资源

"""


class Semaphore:
  def __init__(self, client, name):
    self.client = client
    self.name = name  # 用于存储信号量持有者标识符的集合
    self.holder_key = "semaphore::{0}::holders".format(name)  # 用于记录信号量最大可获取数量的字符串 
    self.size_key = "semaphore::{0}::max_size".format(name)

  def set_max_size(self, size):
    """ 设置信号量的最大可获取数量"""
    self.client.set(self.size_key, size)

  def get_max_size(self):
    """返回信号量的最大可获取数量
    """
    result = self.client.get(self.size_key)
    if result is None:
      return 0
    else:
      return int(result)

  def get_current_size(self):
    """返回目前已被获取的信号量数量"""
    return self.client.scard(self.holder_key)

  def acquire(self, identity):
    """尝试获取一个信号量，成功时返回True，失败时返回False. 传入的identity 参数将被用于标识客户端的身份。
    如果调用该方法时信号量的最大可获取数量尚未被设置，那么将引发一个TypeError
    """
    # 开启流水线 
    pipe = self.client.pipeline()
    try:
      # 监视与信号量有关的两个键 
      pipe.watch(self.size_key, self.holder_key)
      # 取得当前已被获取的信号量数量，以及最大可获取的信号量数量 
      current_size = pipe.scard(self.holder_key)
      max_size_in_str = pipe.get(self.size_key)
      if max_size_in_str is None:
        raise TypeError("Semaphore max size not set")
      else:
        max_size = int(max_size_in_str)
        if current_size < max_size:
          # 如果还有剩余的信号量可用 
          # 那么将给定的标识符放入持有者集合中 
          pipe.multi()
          pipe.sadd(self.holder_key, identity)
          pipe.execute()
          return True
        else:
          # 没有信号量可用，获取失败 
          return False
    except WatchError:
      # 获取过程中有其他客户端修改了size_key 或者holder_key，获取失败 
      return False
    finally:
      # 取消监视 
      pipe.unwatch()
      # 将连接归还给连接池
      pipe.reset()


def release(self, identity):
  """根据给定的标识符，尝试释放当前客户端持有的信号量。
  返回True 表示释放成功，返回False 表示由于标识符不匹配而导致释放失败
  """
  # 尝试从持有者集合中移除给定的标识符
  result = self.client.srem(self.holder_key, identity)  # 移除成功则说明信号量释放成功 
  return result == 1


if __name__ == '__main__':
  client = Redis(decode_responses=True)
  semaphore = Semaphore(client, "test-semaphore")  # 创建计数信号量 
  semaphore.set_max_size(3)  # 设置信号量的最大可获取数量 
  semaphore.acquire("peter")  # 获取信号量         True 
  semaphore.acquire("jack")  # True 
  semaphore.acquire("tom")  # True 
  semaphore.acquire("mary")  # 可用的3个信号量都已被获取，无法取得更多信号量         False
  semaphore.release("jack")  # 释放一个信号量         True 
  semaphore.get_current_size()  # 目前有两个信号量已被获取         2 
  semaphore.get_max_size()  # 信号量的最大可获取数量为3个         3
